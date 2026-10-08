// ============================================================
//  DAYNEIRA — inicio.js
// ============================================================

const DESCRIPCION = "Con Dayneira vas a poder crear tus propios libros personalizados paso a paso de la manera más sencilla. Aprende a crear tus personajes, tu estética, tu mundo y sobre todo a desbloquear tu creatividad haciendo tangible lo que mantiene tu mente encubierto.";

function revelarApp() {
    const btn = document.getElementById('btnDescubrir');
    const textoEl = document.getElementById('descripcionTexto');
    const secciones = document.getElementById('seccionesWrapper');

    btn.disabled = true;
    btn.style.opacity = '0.5';

    escribirTexto(textoEl, DESCRIPCION, () => {
        setTimeout(() => {
            secciones.classList.add('visible');
            animarCards();
            secciones.scrollIntoView({ behavior: 'smooth' });
        }, 300);
    });
}

// Velocidad máxima: 4ms por letra
function escribirTexto(elemento, texto, callback) {
    elemento.classList.add('visible');
    elemento.textContent = '';
    let i = 0;
    const intervalo = setInterval(() => {
        elemento.textContent += texto[i];
        i++;
        if (i >= texto.length) {
            clearInterval(intervalo);
            if (callback) callback();
        }
    }, 4);
}

function animarCards() {
    document.querySelectorAll('.seccion-card').forEach((card, index) => {
        setTimeout(() => card.classList.add('animada'), index * 80);
    });
}

function toggleMenu() {
    document.getElementById('userDropdown').classList.toggle('abierto');
}

document.addEventListener('click', (e) => {
    const wrapper = document.querySelector('.user-menu-wrapper');
    if (wrapper && !wrapper.contains(e.target)) {
        document.getElementById('userDropdown').classList.remove('abierto');
    }
});

function toggleModoOscuro() {
    document.body.classList.toggle('oscuro');
    const esOscuro = document.body.classList.contains('oscuro');
    document.getElementById('btnModoOscuro').textContent = esOscuro ? '☀️ Modo claro' : '🌙 Modo oscuro';
    localStorage.setItem('dayneira-oscuro', esOscuro);
}

function compartir() {
    if (navigator.share) {
        navigator.share({ title: 'Dayneira', text: '¡Descubre Dayneira, tu espacio para crear libros!', url: window.location.origin });
    } else {
        navigator.clipboard.writeText(window.location.origin);
        alert('¡Enlace copiado al portapapeles!');
    }
}

function cambiarIdioma(idioma) {
    alert('Próximamente disponible en más idiomas ✨');
}

window.addEventListener('load', () => {
    if (localStorage.getItem('dayneira-oscuro') === 'true') {
        document.body.classList.add('oscuro');
        document.getElementById('btnModoOscuro').textContent = '☀️ Modo claro';
    }
});