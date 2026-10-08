package app.datos;

import app.model.Curiosidad;
import java.util.*;

public class CuriosidadesData {

    private static final Map<String, Curiosidad> DATOS = new LinkedHashMap<>();

    static {
        agregar("arena-negra", "mundo", "Playas de arena negra",
                "Existen playas con arena completamente negra por las rocas volcanicas que el mar ha triturado durante miles de anios.",
                "Las playas de arena negra existen en lugares con actividad volcanica intensa: Islandia, Hawaii, Canarias o el sur de Italia. La arena negra es mas pesada, se calienta mas rapido y tiene una textura diferente a la dorada. En Islandia, la playa Reynisfjara tiene ademas columnas de basalto hexagonales formadas de manera natural, como si alguien las hubiera tallado. El contraste entre arena negra, mar gris y cielo blanco crea un paisaje de otro planeta. En Hawaii, la playa Punaluu tiene tortugas marinas que suben a descansar sobre la arena oscura.",
                "Que significado simbolico podria tener una playa negra para una cultura ficticia? Seria un lugar sagrado, maldito, o un portal?");

        agregar("mar-estrellas", "mundo", "El mar que brilla de noche",
                "En las Maldivas hay una playa donde el mar brilla de noche con luz azul. Son organismos bioluminiscentes que reaccionan al movimiento.",
                "La bioluminiscencia es la capacidad de producir luz propia mediante reacciones quimicas. En el mar, son el fitoplancton y ciertas medusas quienes crean este efecto. Cuando el agua se agita, los organismos emiten destellos azules o verdes. El espectaculo depende de la temperatura del agua, la fase lunar y la cantidad de fitoplancton, no ocurre todas las noches. Tambien se ve en algunas bahias de Puerto Rico, en la costa de Nueva Zelanda y en el Mediterraneo. Las culturas antiguas creian que el mar brillante era una senal de los dioses.",
                "Como usarias la bioluminiscencia en tu historia? Podria ser un sistema de comunicacion, una senal de peligro, o tener propiedades magicas?");

        agregar("blaschko", "cuerpo", "Las lineas de Blaschko",
                "El cuerpo humano tiene lineas invisibles que solo se ven bajo luz ultravioleta o en personas con dos ADN distintos.",
                "Alfred Blaschko fue un dermatologo aleman que en 1901 describio patrones que aparecen en la piel en ciertas condiciones. Estas lineas siguen el camino que recorrieron las celulas durante el desarrollo embrionario. En personas con quimerismo, que tienen dos tipos de ADN porque dos embriones se fusionaron en los primeros dias, las lineas aparecen como franjas de piel con colores ligeramente distintos. Las formas varian segun la zona del cuerpo: rayas en V en el tronco, lineas longitudinales en los brazos, remolinos en la cabeza. Es como si cada persona llevara un mapa secreto de su propio origen.",
                "Que pasaria si en tu mundo estas lineas fueran visibles para todos? Como cambiaria la sociedad o la identidad de los personajes?");

        agregar("lalibela", "historia", "Las iglesias excavadas hacia abajo",
                "En Etiopia, las iglesias de Lalibela fueron esculpidas en la roca hacia abajo, como edificios hundidos en el suelo.",
                "Las once iglesias rupestres de Lalibela fueron talladas en el siglo XII por orden del rey Lalibela. No son cuevas adaptadas, son edificios completos esculpidos en roca volcanica solida, tallando hacia abajo desde la superficie. Los trabajadores excavaron zanjas alrededor de bloques de roca y luego esculpieron el interior, creando iglesias con columnas, arcos y habitaciones. La mas famosa, Bete Giyorgis, tiene forma de cruz perfecta vista desde arriba. Estan conectadas por tuneles tallados en la roca. Hoy siguen siendo iglesias activas. La leyenda dice que los angeles trabajaban de noche para avanzar el doble.",
                "Que civilizacion construiria hacia abajo en lugar de hacia arriba? Que razones religiosas o culturales podrian tener?");

        agregar("moai-cuerpo", "historia", "Los Moai tienen cuerpo",
                "Las famosas estatuas de la Isla de Pascua no son solo cabezas, tienen cuerpos completos enterrados bajo tierra.",
                "Durante siglos solo se conocian las cabezas que sobresalen del suelo. Las excavaciones revelaron cuerpos completos enterrados, algunos de mas de 7 metros de altura total y 80 toneladas de peso. En la espalda de algunas estatuas hay inscripciones en Rongorongo, un sistema de escritura unico aun sin descifrar. Los Rapa Nui los transportaban sin ruedas ni animales, haciendolos caminar oscilando con cuerdas. La civilizacion que los construyo desaparecio antes de la llegada europea.",
                "Que secreto podria esconder el cuerpo enterrado de una estatua en tu historia? Que revelaria al desenterrarse?");

        agregar("dragones", "mitologia", "Los dragones en todas las culturas",
                "Culturas que nunca se conocieron llegaron todas a inventar la misma criatura: un gran reptil alado.",
                "Los dragones aparecen en mitologias de culturas que jamas tuvieron contacto: China, Europa medieval, Mesopotamia sumeria, los Aztecas, pueblos indigenas de America del Norte, el antiguo Egipto. Todos describen criaturas similares: grandes, poderosas, asociadas al fuego o al agua. En Occidente el dragon es el enemigo a vencer; en Asia, simbolo de poder y buena suerte. Algunas teorias proponen que el mito surgio cuando los humanos encontraban huesos de dinosaurios. Otras sugieren que el miedo instintivo a serpientes y grandes depredadores se fusiono en una sola criatura.",
                "Por que todas las culturas necesitaron inventar una criatura asi? Que funcion cumplia en sus historias?");

        agregar("azul-color", "creatividad", "El color que no existia",
                "Los antiguos griegos no tenian palabra para el color azul. Homero describe el mar como color de vino.",
                "El linguista William Gladstone analizo en 1858 los textos de Homero y descubrio que nunca menciona el azul. El cielo es bronce, el mar es color de vino oscuro. Investigaciones posteriores confirmaron que el griego antiguo no tenia palabra para el azul. Tampoco el japones antiguo, el chino clasico ni el hebreo biblico. El azul fue el ultimo color en recibir nombre en casi todas las culturas. Los estudios muestran que las personas sin nombre para un color tienen mas dificultad para distinguirlo visualmente. El lenguaje no solo describe la realidad, la crea.",
                "Que ocurriria si una cultura en tu historia no pudiera ver cierto color porque no tiene palabra para el? Que pasaria al descubrirlo?");

        agregar("tolkien", "escritura", "Tolkien construyo el idioma primero",
                "Tolkien invento el Quenya y el Sindarin decadas antes de escribir El Senor de los Anillos.",
                "Tolkien era profesor de linguistica en Oxford. Su pasion era inventar idiomas, no contar historias. Comenzo el Quenya cuando tenia veinte anios, mucho antes de que existiera la Tierra Media. El mundo surgio como necesidad: queria que sus idiomas tuvieran hablantes e historia. El Quenya esta inspirado en el fines; el Sindarin, en el gales. Ambos tienen gramatica completa, vocabulario extenso y sistema de escritura propio. Tolkien decia que inventar un idioma sin una mitologia era como construir una casa sin cimientos.",
                "Que revelaria el idioma de tu mundo sobre la cultura que lo habla? Que palabras existirian que no existen en ningun idioma real?");

        agregar("pinturas", "historia", "El cine tiene 30.000 anios",
                "Algunas pinturas rupestres estan hechas en paredes irregulares a proposito para que parezcan animadas con luz de antorcha.",
                "El arqueologo Marc Azema descubrio que en cuevas como Lascaux o Altamira, los animales estan pintados en paredes con relieves que no son accidentales. Con una antorcha en movimiento, los bisontes parecen correr y las figuras cobran vida. Tambien se han encontrado discos de hueso con figuras en distintas posiciones que, al girarse, crean una imagen en movimiento. Los humanos del Paleolitico no solo pintaban: contaban historias con luz y movimiento en cuevas que actuaban como cines naturales.",
                "Que nos dice sobre los humanos que su primer arte ya fuera cinematografico y narrativo? Como usarias esto en una historia?");

        agregar("diluvio", "mitologia", "El diluvio en 200 culturas",
                "Mas de 200 culturas distintas tienen un mito de un gran diluvio que casi destruyo la humanidad.",
                "Los sumerios tenian el mito de Utnapishtim, anterior al texto biblico. Los griegos tenian a Deucalion. Los Mayas lo describiban en el Popol Vuh. Pueblos de America del Norte, Australia, China e India tienen versiones propias. Una teoria reciente propone que el mito podria tener origen real: hace 7.600 anios el Mar Negro era un lago de agua dulce. Cuando el Mediterraneo subio, el agua rompio el Bosforo y anego una enorme region fertil. Las personas que huian podrian haber llevado el relato a todos los rincones del mundo.",
                "Que diria el mito del diluvio sobre las culturas que lo cuentan? Como usarias un diluvio como punto de inicio de una historia?");
    }

    private static void agregar(String id, String cat, String titulo,
                                String resumen, String detalle, String preguntaIA) {
        DATOS.put(id, new Curiosidad(id, "", cat, titulo, resumen, detalle, preguntaIA));
    }

    public static List<Curiosidad> getTodas() { return new ArrayList<>(DATOS.values()); }
    public static Curiosidad getPorId(String id) { return DATOS.get(id); }
}