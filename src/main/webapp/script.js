'use strict';

const form = document.getElementById('form');
const xInput = document.getElementById('x');
const rSelect = document.getElementById('r');
const errBox = document.getElementById('error');
const canvas = document.getElementById('plot');
const ctx = canvas.getContext('2d');

const UNIT_SIZE = 45;
const AXIS_LIMIT = 4;
const center_x = canvas.width / 2;
const center_y = canvas.height / 2;

const currentR = () => (rSelect.value === '' ? null : Number(rSelect.value));
const showError = msg => { errBox.textContent = msg || ''; };

function formatAllTimestamps() {
    const timeCells = document.querySelectorAll('.time-cell');
    timeCells.forEach(cell => {
        const rawTime = cell.getAttribute('data-iso');
        if (!rawTime) return;
        const dateObj = new Date(rawTime);
        if (isNaN(dateObj.getTime())) return;
        cell.textContent = new Intl.DateTimeFormat('ru-RU', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        }).format(dateObj);
    });
}

function validateXStrict(rawVal) {
    let str = rawVal.trim().replace(',', '.');
    if (str === '') {
        return { valid: false, error: 'Заполните поле X' };
    }

    const regex = /^[+-]?(\d+(\.\d*)?|\.\d+)$/;
    if (!regex.test(str)) {
        return { valid: false, error: 'X должен быть числом' };
    }

    let isNegative = str.startsWith('-');
    let absStr = str.replace(/^[+-]/, '');
    if (absStr.startsWith('.')) absStr = '0' + absStr;

    let [intPart, decPart = ''] = absStr.split('.');
    intPart = intPart.replace(/^0+/, '') || '0';
    decPart = decPart.replace(/0+$/, '');

    if (intPart.length > 1 || parseInt(intPart, 10) >= 3) {
        return {
            valid: false,
            error: isNegative ? 'X должен быть строго больше -3' : 'X должен быть строго меньше 3'
        };
    }

    return { valid: true, value: str };
}

function validateForm() {
    const xValidation = validateXStrict(xInput.value);
    if (!xValidation.valid) return xValidation.error;

    if (!form.querySelector('input[name="y"]:checked')) return 'Выберите значение Y';
    if (currentR() === null) return 'Выберите радиус R';
    return null;
}

form.addEventListener('submit', e => {
    const msg = validateForm();
    showError(msg);
    if (msg) {
        e.preventDefault();
    } else {
        xInput.value = xInput.value.trim().replace(',', '.');
    }
});

function drawAxes(r) {
    ctx.strokeStyle = '#000';
    ctx.lineWidth = 1;
    ctx.fillStyle = '#000';
    ctx.font = '12px Arial';

    ctx.beginPath();
    ctx.moveTo(0, center_y); ctx.lineTo(canvas.width, center_y);
    ctx.moveTo(center_x, 0); ctx.lineTo(center_x, canvas.height);

    ctx.moveTo(canvas.width - 10, center_y - 4);
    ctx.lineTo(canvas.width, center_y);
    ctx.lineTo(canvas.width - 10, center_y + 4);

    ctx.moveTo(center_x - 4, 10);
    ctx.lineTo(center_x, 0);
    ctx.lineTo(center_x + 4, 10);
    ctx.stroke();

    ctx.fillText('x', canvas.width - 12, center_y - 8);
    ctx.fillText('y', center_x + 8, 12);

    for (let i = -AXIS_LIMIT; i <= AXIS_LIMIT; i++) {
        if (i === 0) continue;
        let px = center_x + i * UNIT_SIZE;
        let py = center_y - i * UNIT_SIZE;

        ctx.beginPath();
        ctx.moveTo(px, center_y - 4);
        ctx.lineTo(px, center_y + 4);
        ctx.moveTo(center_x - 4, py);
        ctx.lineTo(center_x + 4, py);
        ctx.stroke();

        let label = (r === null) ?
            (i === 3 ? "R" : i === -3 ? "-R" : "") : String(i);

        if (label) {
            ctx.fillText(label, px - 6, center_y + 16);
            ctx.fillText(label, center_x + 8, py + 4);
        }
    }
}

function drawShapes(r) {
    ctx.fillStyle = '#3399ff';

    ctx.beginPath();
    ctx.moveTo(center_x - (r / 2) * UNIT_SIZE, center_y);
    ctx.lineTo(center_x, center_y - r * UNIT_SIZE);
    ctx.lineTo(center_x, center_y);
    ctx.closePath();
    ctx.fill();

    ctx.beginPath();
    ctx.moveTo(center_x, center_y);
    ctx.arc(center_x, center_y, r * UNIT_SIZE, -Math.PI / 2, 0, false);
    ctx.closePath();
    ctx.fill();

    ctx.fillRect(center_x, center_y, r * UNIT_SIZE, (r / 2) * UNIT_SIZE);
}

function drawPoint(x, y, hit) {
    const px = center_x + x * UNIT_SIZE;
    const py = center_y - y * UNIT_SIZE;

    ctx.beginPath();
    ctx.arc(px, py, 4, 0, Math.PI * 2);
    ctx.fillStyle = hit ? '#47da8c' : '#da2020';
    ctx.fill();
    ctx.strokeStyle = '#000';
    ctx.lineWidth = 1;
    ctx.stroke();
}

function redraw() {
    const r = currentR();
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    if (r !== null) {
        drawShapes(r);
    }
    drawAxes(r);

    if (r !== null && typeof POINTS !== 'undefined') {
        for (const p of POINTS) {
            if (Number(p.r) === r) {
                drawPoint(Number(p.x), Number(p.y), p.hit);
            }
        }
    }
}

rSelect.addEventListener('change', () => {
    showError('');
    redraw();
});

canvas.addEventListener('click', ev => {
    const r = currentR();
    if (r === null) {
        showError('Невозможно определить координаты: сначала выберите R');
        return;
    }

    const box = canvas.getBoundingClientRect();
    const k = canvas.width / box.width;
    const x = ((ev.clientX - box.left) * k - center_x) / UNIT_SIZE;
    const y = (center_y - (ev.clientY - box.top) * k) / UNIT_SIZE;

    if (x <= -3 || x >= 3) {
        showError('Клик вне допустимого диапазона X (-3 ... 3)');
        return;
    }
    showError('');

    const f = document.createElement('form');
    f.method = 'post';
    f.action = CONTROLLER_URL;
    const data = {
        x: x.toFixed(6),
        y: y.toFixed(6),
        r: String(r),
        source: 'graph'
    };

    for (const [name, value] of Object.entries(data)) {
        const i = document.createElement('input');
        i.type = 'hidden';
        i.name = name;
        i.value = value;
        f.appendChild(i);
    }
    document.body.appendChild(f);
    f.submit();
});

formatAllTimestamps();
redraw();