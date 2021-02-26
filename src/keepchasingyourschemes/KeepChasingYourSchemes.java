package keepchasingyourschemes;

import java.util.*;

public class KeepChasingYourSchemes {
    static Random random = new Random();
    static Scanner scanner = new Scanner(System.in);
    //Registre de globus. Clau = id del globus
    static Map<Integer,Globus> registreDeGlobus = new HashMap<Integer,Globus>();
    
    static int compte = 10000; //Compte corrent
    
    public static void main(String[] args) {
        while(true) {
            Opcions opcio = PrintMenu("Menú principal",
                new OpcioMenu("Construir un globus",Opcions.construirGlobus,true),
                new OpcioMenu("Fer un viatge",Opcions.ferViatge,GlobusDisponibles().size() > 0,"No hi han globus disponibles."),
                new OpcioMenu("Fer tornar el globus",Opcions.tornarGlobus,GlobusVolant().size() > 0,"No hi han globus surcant el cel."),
                new OpcioMenu("Vendre un globus",Opcions.vendreGlobus,GlobusDisponibles().size() > 0,"No queden globus disponibles."),
                new OpcioMenu("Comprovar situació",Opcions.comprovar,true),
                new OpcioMenu("Sortir",Opcions.sortir,true));
            
            if(opcio == Opcions.sortir) break; //Sortir
            
            switch(opcio) {
                case construirGlobus: ConstruirGlobus(); break;
                case ferViatge: FerViatge(); break;
                case tornarGlobus: FerTornarGlobus(); break;
                case vendreGlobus: VendreGlobus(); break;
                case comprovar: ComprovarSituacio(); break;
            }
        }
        System.out.println("-- Final de programa --");
    }
    
    static void ConstruirGlobus() {
        System.out.println("-- Ho has de comprar tot per poder construir el globus --");
        Compra compra = new Compra("COMPRA D'UN GLOBUS");
        Ask("Vols comprar una cistella de napicols?");
        if(GetYesNo()) {
            compra.Afegir("Cistella de napicols", 1000);
        } else return;
        Ask("Vols comprar una bandera?");
        if(GetYesNo()) {
            compra.Afegir("Bandera", 1000);
        } else return;
        Ask("Vols comprar gomes?");
        if(GetYesNo()) {
            compra.Afegir("Gomes",1000);
        } else return;
        if(!GastarPasta(compra.Total()).ok) return; //Retirem els fons del nostre compte
        compra.ImprimirFactura();
        System.out.println("S'està construint el globus...");
        System.out.println("Globus construit!");
        Ask("Quin serà el seu número identificatiu?");
        int codiGlobus;
        while(true) {
            codiGlobus = GetNumber(0);
            if(!registreDeGlobus.containsKey(codiGlobus)) break;
            Error("Ja tenim un globus amb aquest identificador.");
        }
        registreDeGlobus.put(codiGlobus, new Globus(codiGlobus));
    }
    static void FerViatge() {
        Ask("Quanta gent pujarà al globus?");
        int gent = GetNumber(1,4);
        Compra compra = new Compra("BITLLETS");
        compra.Afegir(gent + (gent == 1 ? " bitllet." : "bitllets."), gent * 100);
        IngressarPasta(compra.Total()); //Ingressem els diners al compte
        Integer globus = SeleccionarGlobus();
        System.out.println("Es farà el viatge amb el globus N " + globus + ".");
        Globus globusSeleccionat = registreDeGlobus.get(globus);
        globusSeleccionat.deViatge = true;
    }
    static void FerTornarGlobus() {
        System.out.println("Globus viatjant:");
        List<Integer> claus = new LinkedList<Integer>();
        for(Integer id: registreDeGlobus.keySet()) {
            Globus globus = registreDeGlobus.get(id);
            if(globus.deViatge) {
                System.out.println("\t|--> ID: " + id);
                claus.add(id);
            }
        }
        Ask("Quin globus vols fer tornar?");
        int id = GetNumber(claus); //Escollim id entre les id disponibles
        Globus globus = registreDeGlobus.get(id);
        globus.AcabarViatge();
    }
    static void VendreGlobus() {
        System.out.println("-- Nomès pots vendre globus que no estan viatjant --");
        ImprimirFlota(true,true);
        Ask("Quin globus vols vendre?");
        int id = GetNumber(GlobusDisponibles());
        Globus globus = registreDeGlobus.get(id);

        //Procés de venta
        int valorGlobus = random.nextInt(21) % 2 == 0 ? globus.viatges * 100 : globus.viatges * 1000;
        System.out.println("S'ha venut el globus " + id + " per " + valorGlobus + "€.");
        IngressarPasta(valorGlobus);
        registreDeGlobus.remove(id); //Eliminar globus
    }
    static void ComprovarSituacio() {
        System.out.println("*** COMPROVAR SITUACIÓ ***");
        ImprimirFlota(false,true);
        System.out.println("Diners al compte: " + compte + "€\n");
    }

    static int SeleccionarGlobus() {
        List<Integer> claus = GlobusDisponibles();
        System.out.println("Globus disponibles:");
        /*for(int i = 1; i <= claus.size(); i++)
            System.out.println("Globus " + claus.get(i-1));*/
        ImprimirFlota(true,false);
        Ask("Quin globus vols?");
        return GetNumber(claus);
    }
    static List<Integer> GlobusDisponibles() {
        Set<Integer> keys = registreDeGlobus.keySet();
        List<Integer> availableKeys = new LinkedList<Integer>();
        for(int key: keys) {
            if(GlobusDisponible(key)) continue;
            availableKeys.add(key);
        }
        return availableKeys;
    }
    static boolean GlobusDisponible(int key) {
        return registreDeGlobus.get(key).deViatge;
    }
    static List<Integer> GlobusVolant() {
        List<Integer> claus = new LinkedList<Integer>();
        for(Globus globus: registreDeGlobus.values()) if(globus.deViatge) claus.add(globus.id);
        return claus;
    }

    static Flag GastarPasta(int diners) {
        if(compte < diners) return new Flag(false,"No tens suficients diners!");
        
        compte -= diners;
        //Transacció completada
        System.out.println("S'han restat " + diners + "€ del teu compte.\nDiners restants: " + compte + "€");
        return new Flag(true);
    } 
    static void IngressarPasta(int diners) {
        compte += diners;
    }

    static Opcions PrintMenu(String titol, OpcioMenu... opcions) {
        System.out.println(titol);
        for(int i = 0; i < opcions.length; i++) opcions[i].Print(i + 1);
        while(true) {
            System.out.print("\nIntrodueix una opció: ");
            int op = GetNumber(1,opcions.length) - 1;
            if(opcions[op].habilitat) return opcions[op].opcio;
            else Error(opcions[op].textDeshabilitat);
        }
    }
    static boolean GetYesNo() {
        while(true) {
            String response = scanner.next();
            if(response.toLowerCase().equals("s") || response.toLowerCase().equals("si")) return true;
            if(response.toLowerCase().equals("n") || response.toLowerCase().equals("no")) return false;
            Error("Has d'introduir \"si\" o \"no\"");
        }
    }
    static void ImprimirFlota(boolean amagarNoDisponibles, boolean mostrarViatges) {
        System.out.println("FLOTA: " + registreDeGlobus.size() + " globus");
        for(Integer id: registreDeGlobus.keySet()) {
            Globus globus = registreDeGlobus.get(id);
            if(amagarNoDisponibles && globus.deViatge) continue;
            System.out.print("\t|--> ID: " + globus.id + " --- " + (globus.deViatge ? "[VIATJANT]" : "[DISPONIBLE]"));
            if(mostrarViatges) System.out.print(" Viatges: " + globus.viatges);
            System.out.println();
        }
    }
    static int GetNumber(List<Integer> values) {
        while(true) {
            int valor = scanner.nextInt();
            if(values.contains(valor)) return valor;
            Error("Valor fora de rang.");
        }
    }
    static int GetNumber(int min, int max) {
        while(true) {
            int select = scanner.nextInt();
            if(select >= min && select <= max) return select;
            Error("Valor fora de rang");
        }
    }
    static int GetNumber(int min) {
        while(true) {
            int select = scanner.nextInt();
            if(select >= min) return select;
            Error("Valor fora de rang");
        }
    }
    static void Error(String text) {
        System.out.println("[!] " + text);
    }
    static void Ask(String text) {
        System.out.print("\n[?] " + text + " ");
    }
    enum Opcions {
        construirGlobus,
        ferViatge,
        tornarGlobus,
        vendreGlobus,
        comprovar,
        sortir
    }
    static class Globus {
        public int id;
        public boolean deViatge = false;
        public int viatges = 0;

        public Globus(int id) {
            this.id = id;
        }
        public void AcabarViatge() {
            viatges++;
            deViatge = false;
            System.out.println("El globus N " + id + " ha tornat del seu viatge N " + viatges + ".");

            if(viatges >= 10) {
                //Final de la vida del globus
                System.out.println("La vida útil del globus N " + id + " ha arribat al seu final.");
                registreDeGlobus.remove(id);
            }
        }
    }
    static class Compra {
        //$$$ Carrito de la compra $$$
        private List<Element> compra = new LinkedList<Element>();
        private String titol = "COMPRA";
        
        public Compra(String titol) {
            this.titol = titol;
        }
        
        public void Afegir(String nom, int preu) {
            compra.add(new Element(nom,preu));
        }
        public void ImprimirFactura() {
            System.out.print("[" + titol + "]");
            for(Element element: compra) {
                System.out.print("\n\t|-->" + element.nom + ": " + element.preu + "€");
            }
            System.out.println("\nTOTAL: " + Total() + "€\n");
        }
        public int Total() {
            int count = 0;
            for(Element element: compra) count += element.preu;
            return count;
        }
        
        static class Element {
            public int preu;
            public String nom;
            
            public Element(String nom, int preu) {
                this.nom = nom;
                this.preu = preu;
            }
        }
    }
    static class Flag {
        boolean ok;
        String error = "Error desconegut";
        
        public Flag(boolean ok) {
            this.ok = ok;
        }
        public Flag(boolean ok, String error) {
            this.ok = ok;
            this.error = error;
            Error(error);
        }
    }
    static class OpcioMenu {
            String text;
            String textDeshabilitat = "Opció deshabilitada pel programa";
            boolean habilitat;
            Opcions opcio;
            
            public OpcioMenu(String text, Opcions opcio, boolean habilitat) {
                this.text = text;
                this.habilitat = habilitat;
                this.opcio = opcio;
            }
            public OpcioMenu(String text, Opcions opcio, boolean habilitat, String textDeshabilitat) {
                this.text = text;
                this.habilitat = habilitat;
                this.textDeshabilitat = textDeshabilitat;
                this.opcio = opcio;
            }
            
            public void Print(int num) {
                System.out.println(num + ". " + text + " " + (habilitat ? "" : "[DESHABILITAT]"));
            }
        }
}