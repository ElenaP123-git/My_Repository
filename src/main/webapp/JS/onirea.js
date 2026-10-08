// ============================================================
//  DAYNEIRA — onirea.js
// ============================================================

let estiloSeleccionado = 'poetico';

// ── Selección de estilo ──
document.querySelectorAll('.estilo-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        document.querySelectorAll('.estilo-btn').forEach(b => b.classList.remove('activo'));
        btn.classList.add('activo');
        estiloSeleccionado = btn.dataset.estilo;
    });
});

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

// ── Enviar idea ──
async function enviarIdea() {
    const idea = document.getElementById('inputIdea').value.trim();
    if (!idea) {
        alert('Escribe tu idea antes de enviar ✍️');
        return;
    }

    const btn = document.getElementById('btnEnviar');
    btn.disabled = true;

    // Mostrar mensaje del usuario
    agregarMensajeUsuario(idea);

    // Mostrar typing
    const typing = document.getElementById('typingIndicator');
    typing.classList.add('visible');
    scrollAbajo();

    // Nombres de estilo para el prompt
    const nombresEstilo = {
        poetico:    'poético y literario, con metáforas sutiles y lenguaje evocador',
        oscuro:     'oscuro y simbólico, con imágenes intensas y cierta melancolía profunda',
        epico:      'épico y grandioso, con fuerza narrativa y sensación de destino inevitable',
        romantico:  'romántico y sensible, que transmita emoción y conexión entre almas',
        misterioso: 'misterioso y enigmático, con capas de significado y atmósfera densa',
        melancolico:'melancólico y nostálgico, con esa belleza triste que queda grabada'
    };

    const prompt = `Eres Onírea, una IA especializada en enriquecer ideas creativas de escritores y artistas.

Tu única función es tomar la idea que te da el autor y expandirla en 3 versiones diferentes de un párrafo, siempre en el estilo solicitado.

REGLAS IMPORTANTES:
- NO inventes personajes, tramas ni elementos que el autor no haya mencionado
- Trabaja SOLO con lo que el autor te da
- Cada versión debe ser un párrafo de 3-5 frases
- Usa un lenguaje ${nombresEstilo[estiloSeleccionado]}
- Antes de las versiones, escribe UNA frase corta de introducción
- Formato de respuesta:
  [Una frase de introducción]

  VERSIÓN 1:
  [párrafo]

  VERSIÓN 2:
  [párrafo]

  VERSIÓN 3:
  [párrafo]

La idea del autor es: "${idea}"`;

    try {
        const res = await fetch('/DayneiraApp/onirea/chat', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ prompt })
        });

        const data = await res.json();
        typing.classList.remove('visible');

        if (data.respuesta) {
            agregarMensajeOnirea(data.respuesta);
        } else {
            agregarMensajeOnirea('Hubo un problema al conectar con Onírea. Inténtalo de nuevo.');
        }
    } catch (e) {
        typing.classList.remove('visible');
        agregarMensajeOnirea('Error de conexión. Comprueba que el servidor está activo.');
    }

    btn.disabled = false;
    document.getElementById('inputIdea').value = '';
    scrollAbajo();
}

// ── Renderizar mensaje del usuario ──
function agregarMensajeUsuario(texto) {
    const chat = document.getElementById('chatMensajes');
    const div = document.createElement('div');
    div.className = 'mensaje mensaje-usuario';
    div.innerHTML = `
        <div class="mensaje-avatar">✍️</div>
        <div class="mensaje-burbuja">
            <p><strong>Estilo:</strong> ${estiloSeleccionado}</p>
            <p>${escapar(texto)}</p>
        </div>`;
    chat.appendChild(div);
}

// ── Renderizar respuesta de Onírea ──
function agregarMensajeOnirea(texto) {
    const chat = document.getElementById('chatMensajes');
    const div = document.createElement('div');
    div.className = 'mensaje mensaje-onirea';

    // Parsear versiones
    const partes = texto.split(/VERSIÓN \d+:/i).map(s => s.trim()).filter(Boolean);
    let html = '';

    if (partes.length > 1) {
        // Primera parte = introducción
        html += `<p>${escapar(partes[0])}</p>`;
        // Resto = versiones
        for (let i = 1; i < partes.length; i++) {
            html += `
            <div class="version-bloque">
                <div class="version-titulo">Versión ${i}</div>
                <div class="version-texto">${escapar(partes[i])}</div>
            </div>`;
        }
    } else {
        html = `<p>${escapar(texto)}</p>`;
    }

    div.innerHTML = `
        <div class="mensaje-avatar">🌙</div>
        <div class="mensaje-burbuja">${html}</div>`;
    chat.appendChild(div);
}

function limpiarChat() {
    if (!confirm('¿Limpiar la conversación?')) return;
    const chat = document.getElementById('chatMensajes');
    chat.innerHTML = `
        <div class="mensaje mensaje-onirea">
            <div class="mensaje-avatar">🌙</div>
            <div class="mensaje-burbuja">
                <p>Conversación reiniciada. ¿Qué idea quieres explorar ahora?</p>
            </div>
        </div>`;
}

function scrollAbajo() {
    const chat = document.getElementById('chatMensajes');
    setTimeout(() => chat.scrollTop = chat.scrollHeight, 100);
}

function escapar(texto) {
    return texto
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/\n/g, '<br>');
}

// Enviar con Ctrl+Enter
document.getElementById('inputIdea').addEventListener('keydown', (e) => {
    if (e.key === 'Enter' && e.ctrlKey) enviarIdea();
});