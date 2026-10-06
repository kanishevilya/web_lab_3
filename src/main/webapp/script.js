'use strict';

const form = document.getElementById('form');
const xInput = document.getElementById('x');
const rSelect = document.getElementById('r');
const errBox = document.getElementById('error');
const canvas = document.getElementById('plot');
const g = canvas.getContext('2d');

const W = canvas.width, H = canvas.height;
const CX = W / 2, CY = H / 2;
const SCALE = (W / 2 - 25) / 3;           // 1 единица = SCALE пикселей (R до 3)

const px = x => CX + x * SCALE;
const py = y => CY - y * SCALE;
const currentR = () => (rSelect.value === '' ? null : Number(rSelect.value));
const showError = msg => { errBox.textContent = msg || ''; };

/* ---------- Валидация формы ---------- */
function validateForm() {
    const raw = xInput.value.trim().replace(',', '.');
    if (raw === '' || !/^[+-]?(\d+\.?\d*|\.\d+)$/.test(raw)) return 'X должен быть числом';
    const x = Number(raw);
    if (!(x > -3 && x < 3)) return 'X должен быть строго между -3 и 3';
    if (!form.querySelector('input[name="y"]:checked')) return 'Выберите значение Y';
    if (currentR() === null) return 'Выберите радиус R';
    return null;
}

form.addEventListener('submit', e => {
    const msg = validateForm();
    showError(msg);
    if (msg) e.preventDefault();             // не отправлять форму
    else xInput.value = xInput.value.trim().replace(',', '.');
});

/* ---------- Рисование области варианта 469987 ---------- */
function drawArea(r) {
    g.fillStyle = 'rgba(51, 153, 255, 0.85)';
    // II четверть: треугольник (-R/2,0) (0,R) (0,0)
    g.beginPath();
    g.moveTo(px(-r / 2), py(0)); g.lineTo(px(0), py(r)); g.lineTo(px(0), py(0));
    g.closePath(); g.fill();
    // I четверть: четверть круга радиуса R
    g.beginPath();
    g.moveTo(px(0), py(0));
    g.arc(px(0), py(0), r * SCALE, -Math.PI / 2, 0, false);
    g.closePath(); g.fill();
    // IV четверть: прямоугольник R x R/2
    g.fillRect(px(0), py(0), r * SCALE, (r / 2) * SCALE);
}

function drawAxes(r) {
    g.strokeStyle = '#000'; g.fillStyle = '#000'; g.lineWidth = 1;
    g.font = '12px sans-serif';
    g.beginPath();
    g.moveTo(0, CY); g.lineTo(W, CY);
    g.moveTo(CX, 0); g.lineTo(CX, H);
    g.stroke();
    g.fillText('x', W - 12, CY - 6);
    g.fillText('y', CX + 6, 12);
    if (r === null) return;
    const marks = [[-r, '-R'], [-r / 2, '-R/2'], [r / 2, 'R/2'], [r, 'R']];
    for (const [v, name] of marks) {
        g.beginPath();
        g.moveTo(px(v), CY - 3); g.lineTo(px(v), CY + 3);
        g.moveTo(CX - 3, py(v)); g.lineTo(CX + 3, py(v));
        g.stroke();
        g.fillText(name, px(v) - 8, CY + 16);
        g.fillText(name, CX + 7, py(v) + 4);
    }
}

function drawPoints(r) {
    for (const p of POINTS) {
        if (p.r !== r) continue;               // показываем точки для выбранного R
        g.fillStyle = p.hit ? '#0a0' : '#d00';
        g.beginPath(); g.arc(px(p.x), py(p.y), 4, 0, 2 * Math.PI); g.fill();
    }
}

function redraw() {
    const r = currentR();
    g.clearRect(0, 0, W, H);
    if (r !== null) drawArea(r);
    drawAxes(r);
    if (r !== null) drawPoints(r);
}

rSelect.addEventListener('change', () => { showError(''); redraw(); });
redraw();

/* ---------- Клик по графику ---------- */
canvas.addEventListener('click', ev => {
    const r = currentR();
    if (r === null) {
        showError('Невозможно определить координаты: сначала выберите R');
        return;
    }
    const box = canvas.getBoundingClientRect();
    const k = canvas.width / box.width;     // учёт масштабирования canvas в CSS
    const x = ((ev.clientX - box.left) * k - CX) / SCALE;
    const y = (CY - (ev.clientY - box.top) * k) / SCALE;
    if (!(x > -3 && x < 3)) {
        showError('X вне допустимого интервала (-3; 3)');
        return;
    }
    showError('');
    const f = document.createElement('form');
    f.method = 'post';
    f.action = CONTROLLER_URL;
    const data = { x: x.toFixed(4), y: y.toFixed(4), r: String(r), source: 'graph' };
    for (const [name, value] of Object.entries(data)) {
        const i = document.createElement('input');
        i.type = 'hidden'; i.name = name; i.value = value;
        f.appendChild(i);
    }
    document.body.appendChild(f);
    f.submit();
});