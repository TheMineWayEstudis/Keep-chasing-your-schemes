package keepchasingyourschemes;

import java.util.Scanner;

public class KeepChasingYourSchemes {

    static Scanner scanner = new Scanner(System.in);
    static int[] idGlobus = new int[10];
    static boolean[] estaVolant = new boolean[10];
    static int[] trajectes = new int[10];
    
    static int compte = 10000; //Compte corrent
    
    public static void main(String[] args) {
        while(true) {
            Opcions opcio = PrintMenu("Menú principal",
                new OpcioMenu("Construir un globus",Opcions.construirGlobus,true),
                new OpcioMenu("Fer un viatge",Opcions.ferViatge,true),
                new OpcioMenu("Fer tornar el globus",Opcions.tornarGlobus,true),
                new OpcioMenu("Vendre un globus",Opcions.vendreGlobus,true),
                new OpcioMenu("Comprovar situació",Opcions.comprovar,true),
                new OpcioMenu("Sortir",Opcions.sortir,true));
            
            if(opcio == Opcions.sortir) break; //Sortir
            
            switch(opcio) {
                case construirGlobus: ConstruirGlobus(); break;
            }
        }
    }
    
    static void ConstruirGlobus() {
        Ask("Vols comprar una cistella de napicols?");
        if(GetYesNo()) {
            
        }
    }
    
    static Flag GastarPasta(int diners) {
        
    }
    
    static Opcions PrintMenu(String titol, OpcioMenu... opcions) {
        System.out.println(titol);
        for(int i = 0; i < opcions.length; i++) opcions[i].Print(i + 1);
        System.out.print("\nIntrodueix una opció: ");
        while(true) {
            int op = GetNumber(1,opcions.length) - 1;
            if(opcions[op].habilitat) return opcions[op].opcio;
            else Error(opcions[op].textDeshabilitat);
        }
    }
    
    static boolean GetYesNo() {
        while(true) {
            String response = scanner.next();
            if(response.toLowerCase().equals("s")) return true;
            if(response.toLowerCase().equals("n")) return false;
            Error("Has d'introduir \"s\" o \"n\"");   
        }
    }
    static int GetNumber(int min, int max) {
        while(true) {
            int select = scanner.nextInt();
            if(select >= min && select <= max) return select;
            Error("Valor fora de rang");
        }
    }
    static void Error(String text) {
        System.out.println("[!] " + text);
    }
    static void Ask(String text) {
        System.out.print("\n[?] " + text + " ");
    }
    static enum Opcions {
        construirGlobus,
        ferViatge,
        tornarGlobus,
        vendreGlobus,
        comprovar,
        sortir
    }
    static class Flag {
        boolean ok;
        String error = "Error desconegut";
        
        public Flag(boolean ok) {
            this.ok = ok;
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