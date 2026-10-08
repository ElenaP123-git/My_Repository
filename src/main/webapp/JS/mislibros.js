// ============================================================
//  DAYNEIRA — mislibros.js
// ============================================================

// ── Menú usuario ──
function toggleMenu() {
    document.getElementById('userDropdown').classList.toggle('abierto');
}

document.addEventListener('click', (e) => {
    const wrapper = document.querySelector('.user-menu-wrapper');
    if (wrapper && !wrapper.contains(e.target)) {
        const dd = document.getElementById('userDropdown');
        if (dd) dd.classList.remove('abierto');
    }
});

// ── Filtrar por estado ──
function filtrar(btn) {
    document.querySelectorAll('.filtro-btn').forEach(b => b.classList.remove('activo'));
    btn.classList.add('activo');
    const filtro = btn.dataset.filtro;
    document.querySelectorAll('.libro-card').forEach(card => {
        if (filtro === 'todos' || card.dataset.estado === filtro) {
            card.style.display = 'flex';
        } else {
            card.style.display = 'none';
        }
    });
}

// ── Modal nuevo libro ──
function abrirModalNuevo() {
    document.getElementById('modalNuevo').classList.add('abierto');
}

function cerrarModal(id) {
    document.getElementById(id).classList.remove('abierto');
}

function cerrarModalSiOverlay(event, id) {
    if (event.target.id === id) cerrarModal(id);
}

// ── Selección de color ──
function selColor(el) {
    document.querySelectorAll('.color-opt').forEach(c => c.classList.remove('activo'));
    el.classList.add('activo');
    document.getElementById('colorPortadaInput').value = el.dataset.color;
}

// ── Selección de emoji ──
function selEmoji(el) {
    document.querySelectorAll('.emoji-opt').forEach(e => e.classList.remove('activo'));
    el.classList.add('activo');
    document.getElementById('emojiPortadaInput').value = el.textContent;
}

// ── Modal editar ──
function abrirModalEditar(id, titulo, genero, estado, color, emoji) {
    document.getElementById('editTitulo').value = titulo;
    document.getElementById('editGenero').value = genero || '';
    document.getElementById('editEstado').value = estado;

    const form = document.getElementById('formEditar');
    form.action = '/DayneiraApp/mislibros/editar?id=' + id;

    document.getElementById('modalEditar').classList.add('abierto');
}

// ── Confirmar borrar ──
function confirmarBorrar(id, titulo) {
    if (confirm('¿Seguro que quieres borrar "' + titulo + '"? Esta acción no se puede deshacer.')) {
        const form = document.createElement('form');
        form.method = 'post';
        form.action = '/DayneiraApp/mislibros/borrar?id=' + id;
        document.body.appendChild(form);
        form.submit();
    }
}