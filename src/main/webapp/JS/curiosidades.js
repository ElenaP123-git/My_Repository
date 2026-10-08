// DAYNEIRA - curiosidades.js

var DATOS_DEL_DIA = [
    "Existen hormigas que cultivan hongos bajo tierra desde hace 50 millones de anos.",
    "El sonido viaja 4 veces mas rapido en el agua que en el aire.",
    "Hay un lago en Canada llamado Spotted Lake con charcos de colores segun sus minerales.",
    "Las plantas se comunican a traves de redes de hongos subterraneos.",
    "En Japon hay un bosque de bambu donde el viento crea musica constante.",
    "El cerebro humano genera electricidad suficiente para encender una bombilla.",
    "Los pulpos tienen tres corazones y sangre azul.",
    "El Salar de Uyuni en Bolivia se convierte en el espejo mas grande del mundo cuando llueve."
];

window.addEventListener('load', function() {
    var idx = new Date().getDate() % DATOS_DEL_DIA.length;
    document.getElementById('datoDelDia').textContent = DATOS_DEL_DIA[idx];
});

function toggleMenu() {
    document.getElementById('userDropdown').classList.toggle('abierto');
}

document.addEventListener('click', function(e) {
    var wrapper = document.querySelector('.user-menu-wrapper');
    if (wrapper && !wrapper.contains(e.target)) {
        var dd = document.getElementById('userDropdown');
        if (dd) dd.classList.remove('abierto');
    }
});

function filtrarCat(btn) {
    document.querySelectorAll('.cat-btn').forEach(function(b) {
        b.classList.remove('activo');
    });
    btn.classList.add('activo');
    var cat = btn.dataset.cat;
    document.querySelectorAll('.curiosidad-card').forEach(function(card) {
        if (cat === 'todos' || card.dataset.cat === cat) {
            card.classList.remove('oculta');
        } else {
            card.classList.add('oculta');
        }
    });
}

function abrirDetalle(card) {
    var data = card.querySelector('.card-detalle-data');
    document.getElementById('dpEmoji').textContent    = data.querySelector('.d-emoji').textContent;
    document.getElementById('dpCat').textContent      = data.querySelector('.d-cat').textContent;
    document.getElementById('dpTitulo').textContent   = data.querySelector('.d-titulo').textContent;
    document.getElementById('dpTexto').textContent    = data.querySelector('.d-detalle').textContent;
    document.getElementById('dpPregunta').textContent = data.querySelector('.d-pregunta').textContent;
    document.getElementById('detalleOverlay').classList.add('activo');
    document.getElementById('detallePanel').classList.add('activo');
    document.body.style.overflow = 'hidden';
}

function cerrarDetalle() {
    document.getElementById('detalleOverlay').classList.remove('activo');
    document.getElementById('detallePanel').classList.remove('activo');
    document.body.style.overflow = '';
}

document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') cerrarDetalle();
});

async function generarConIA() {
    var btn = document.getElementById('btnGenerar');
    btn.disabled = true;
    btn.textContent = 'Generando...';
    var catActiva = document.querySelector('.cat-btn.activo').dataset.cat;
    var temas = {
        todos: 'variados del mundo, cuerpo humano, historia, mitologia y creatividad',
        mundo: 'sobre el mundo natural y geografia',
        cuerpo: 'sobre el cuerpo humano y biologia',
        historia: 'sobre historia, arte y civilizaciones',
        escritura: 'sobre escritura y literatura',
        mitologia: 'sobre mitologia y folklore',
        creatividad: 'sobre creatividad y el proceso artistico'
    };
    var prompt = 'Genera 3 datos curiosos ' + temas[catActiva] + ' para inspirar escritores. Responde SOLO en JSON: [{"titulo":"...","texto":"..."},{"titulo":"...","texto":"..."},{"titulo":"...","texto":"..."}]';
    try {
        var res = await fetch('/DayneiraApp/curiosidades/ia', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ prompt: prompt })
        });
        var data = await res.json();
        if (data.respuesta) {
            var jsonStr = data.respuesta.trim().replace(/```json/g, '').replace(/```/g, '').trim();
            mostrarDatosIA(JSON.parse(jsonStr));
        }
    } catch(e) {
        alert('Error al conectar. Intentalo de nuevo.');
    }
    btn.disabled = false;
    btn.textContent = 'Generar con Onirea';
}

function mostrarDatosIA(datos) {
    var resultado = document.getElementById('iaResultado');
    var cards = document.getElementById('iaCards');
    cards.innerHTML = datos.map(function(d) {
        return '<div class="ia-card"><h4>' + d.titulo + '</h4><p>' + d.texto + '</p></div>';
    }).join('');
    resultado.style.display = 'block';
    resultado.scrollIntoView({ behavior: 'smooth' });
}