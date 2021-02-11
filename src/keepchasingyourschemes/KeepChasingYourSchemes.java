package keepchasingyourschemes;

import java.util.Scanner;

public class KeepChasingYourSchemes {

    static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        while(true) {
            PrintMenu(
                new OpcioMenu("Construir un globus",Opcions.construirGlobus,true),
                new OpcioMenu("Fer un viatge",Opcions.ferViatge,true));
        }
    }
    
    static Opcions PrintMenu(OpcioMenu... opcions) {
        for(int i = 0; i < opcions.length; i++) opcions[i].Print(i + 1);
        System.out.print("\nIntrodueix una opció: ");
        while(true) {
            int op = GetNumber(1,opcions.length) - 1;
            if(opcions[op].habilitat) return opcions[op].opcio;
            else Error(opcions[op].textDeshabilitat);
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
    static enum Opcions {
        construirGlobus,
        ferViatge,
        tornarGlobus,
        vendreGlobus,
        comprovar,
        sortir
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