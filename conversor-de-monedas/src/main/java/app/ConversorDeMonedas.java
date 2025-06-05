package app;

import app.services.ExchangeRateApi;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConversorDeMonedas {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private static final String[][] CONVERSIONES = {
        {"USD", "ARS"},
        {"ARS", "USD"},
        {"USD", "BRL"},
        {"BRL", "USD"},
        {"USD", "COP"},
        {"COP", "USD"}
    };

    private static final int OPTION_SALIR = 7;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                mostrarMenu();

                int option = readOption(scanner);
                if (option == OPTION_SALIR) {
                    if (confirmarSalida(scanner)) {
                        System.out.println(ANSI_GREEN + "Gracias por usar el conversor. ¡Hasta pronto!" + ANSI_RESET);
                        break;
                    } else {
                        continue;
                    }
                }

                if (option < 1 || option > CONVERSIONES.length) {
                    System.out.println(ANSI_RED + "Selección inválida. Intente de nuevo." + ANSI_RESET);
                    continue;
                }

                double monto = leerMonto(scanner);
                if (monto <= 0) {
                    System.out.println(ANSI_RED + "Monto no válido. Debe ser mayor que cero." + ANSI_RESET);
                    continue;
                }

                try {
                    double resultado = conversion(option, monto);
                    mostrarResultado(monto, resultado, option);
                } catch (IOException | InterruptedException e) {
                    System.out.println(ANSI_RED + "Error durante la conversión. Intente más tarde." + ANSI_RESET);
                }
            }
        } finally {
            scanner.close();
        }
    }

    private static void mostrarMenu() {
        System.out.println(ANSI_CYAN + """
            ****************************************************
            *  Sea bienvenido/a al Conversor de Moneda =)      *
            ****************************************************""" + ANSI_RESET);
        for (int i = 0; i < CONVERSIONES.length; i++) {
            System.out.printf("%d) %s => %s%n", (i + 1), CONVERSIONES[i][0], CONVERSIONES[i][1]);
        }
        System.out.printf("%d) Salir%n", OPTION_SALIR);
        System.out.println("****************************************************");
        System.out.print("Elija una opción válida: ");
    }

    private static int readOption(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }

    private static double leerMonto(Scanner scanner) {
        System.out.print("Digite la cantidad a convertir: ");
        try {
            return scanner.nextDouble();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }

    private static boolean confirmarSalida(Scanner scanner) {
        System.out.print("¿Está seguro que desea salir? (s/n): ");
        String respuesta = scanner.next().trim().toLowerCase();
        return respuesta.equals("s");
    }

    private static double conversion(int opcion, double cantidad) throws IOException, InterruptedException {
        String from = CONVERSIONES[opcion - 1][0];
        String to = CONVERSIONES[opcion - 1][1];
        return ExchangeRateApi.obtenerConversion(from, to, cantidad);
    }

    private static void mostrarResultado(double cantidad, double resultado, int opcion) {
        String from = CONVERSIONES[opcion - 1][0];
        String to = CONVERSIONES[opcion - 1][1];
        String mensaje = String.format(
            "El valor de %.2f [%s] corresponde a %.2f [%s]",
            cantidad, from, resultado, to
        );
        String borde = "*".repeat(mensaje.length());
        System.out.println(ANSI_GREEN + borde);
        System.out.println(mensaje);
        System.out.println(borde + ANSI_RESET);
    }
}
