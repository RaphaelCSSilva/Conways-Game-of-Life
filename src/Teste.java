import java.util.Scanner;

public class Teste {
    public static void main(String[] args) {
        Scanner qasd = new Scanner(System.in);

        System.out.println("Introduza o numero de pessoas que se inscreveram de manha?");
        double manhaInscricoes = Double.parseDouble(qasd.nextLine());

        System.out.println("Introduza o numero de pessoas que se inscreveram de tarde?");
        double tardeInscricoes = Double.parseDouble(qasd.nextLine());

        System.out.println("Introduza o numero de pessoas que se apareceram de manha?");
        double manhaApareceram = Double.parseDouble(qasd.nextLine());

        System.out.println("Introduza o numero de pessoas que se apareceram de tarde?");
        double tardeApareceram = Double.parseDouble(qasd.nextLine());

        double maisInscricoes = 0;
        if (manhaInscricoes > tardeInscricoes) {
            maisInscricoes = manhaInscricoes;
        } else if (tardeInscricoes > manhaInscricoes) {
            maisInscricoes = tardeInscricoes;
        } else {
            maisInscricoes = manhaInscricoes;
        }

        double percentagemManha = (double) (manhaApareceram / manhaInscricoes) * 100;
        double percentagemTarde = (double) (tardeApareceram / tardeInscricoes) * 100;

        System.out.printf("O turno com mais inscricoes teve %f inscricoes.\n", maisInscricoes);
        System.out.printf("Apareceram %% %f dos inscritos no turno da manha.\n", percentagemManha);
        System.out.printf("Apareceram %% %f dos inscritos no turno da tarde.\n", percentagemTarde);

    }
}
