package app;

import app.services.ExchangeRateApi;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConversorDeMonedas {

    private static final String MENU = """
        ****************************************************
        *  Sea bienvenido/a al Conversor de Moneda =)      *
        ****************************************************
        1) Dólar => Peso argentino
        2) Peso argentino => Dólar
        3) Dólar => Real brasileño
        4) Real brasileño => Dólar
        5) Dólar => Peso colombiano
        6) Peso colombiano => Dólar
        7) Salir
        ****************************************************
        Elija una opción válida:
        """;

    private static final String[][] CONVERCIONES = {
        {"USD", "ARS"},
        {"ARS", "USD"},
        {"USD", "BRL"},
        {"BRL", "USD"},
        {"USD", "COP"},
        {"COP", "USD"}};

    private static final int OUTPUT_VALUE = 7;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println(MENU);
            try {
                int conversor = scanner.nextInt();
                if (conversor == OUTPUT_VALUE){
                    break;
                }
                if (conversor > OUTPUT_VALUE || conversor < 1){
                    System.out.println("Selección Invalida, Vuelva a Intentarlo");
                    continue;
                }
                System.out.println("Digite la cantidad a convertir: ");
                double amount = scanner.nextDouble();
                double resultado = conversion(conversor, amount);
                String response = String.format(
                    "El valor de %.2f [%s] corresponde al valor final de =>>> %.2f [%s]",
                    amount,
                    CONVERCIONES[conversor - 1][0],
                    resultado,
                    CONVERCIONES[conversor - 1][1]
                );
                System.out.println(response);
            } catch (InputMismatchException e){
                System.out.println("Error al ingresar datos");
                System.out.println("Vuelva ha intentarlo");
            } catch (IOException e) {
                System.out.println("Lo siento hubo un error");
            } catch (InterruptedException e) {
                System.out.println("Vuelve a intentarlo mas tarde");
            }
        }
    }

    public static double conversion(int conversor, double amount) throws IOException, InterruptedException {
        return ExchangeRateApi.obtenerConversion(CONVERCIONES[conversor-1][0], CONVERCIONES[conversor-1][1], amount);
    }
}
