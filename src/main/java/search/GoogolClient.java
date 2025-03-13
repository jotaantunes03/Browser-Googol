package search;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.net.URL;

public class GoogolClient {

    private Scanner scanner;
    private static IndexStorageBarrelInterface indexStorageBarrelInterface;
    private static URLQueueInterface urlQueueInterface;



    public GoogolClient() {
        scanner = new Scanner(System.in);
    }


    public static void main(String[] args) {
        try {
            GoogolClient client = new GoogolClient();

            // Conectar ao servidor IndexStorageBarrel RMI
            Registry registry = LocateRegistry.getRegistry(8183);
            indexStorageBarrelInterface = (IndexStorageBarrelInterface) registry.lookup("index");

            // Conectar ao servidor URLQueue RMI
            Registry registryUrlQueue = LocateRegistry.getRegistry(8184);
            urlQueueInterface = (URLQueueInterface) registryUrlQueue.lookup("URLQueueService");

            client.menu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void menu(){

        try {
            // Boleano para verificar se o utilizador quis terminar o servidor
            boolean stopServer = false;
            while (!stopServer) {

                // Menu de opções:
                System.out.println("<<<<<<<<Googol>>>>>>>>");
                System.out.println(
                                  "[1]: Indexar novo URL\n"
                                + "[2]: Realizar uma pesquisa\n"
                                + "[3]: Consultar ligações para uma página específica\n"
                                + "[4]: Ver Estatísticas do Sistema\n"
                                + "[5]: Configurações do Sistema\n"
                                + "[6]: Ajuda\n"
                                + "[7]: Sair\n");

                System.out.print("OPÇÃO: ");
                String userOption = scanner.nextLine();
                clearConsole();

                switch (userOption) {
                    case "1" -> {
                        clearConsole();
                        String userUrl;
                        while (true) {
                            System.out.println("Insira [exit] para voltar atrás.");
                            System.out.print("Inserir URL: ");
                            userUrl = scanner.nextLine();
                            if (userUrl.equalsIgnoreCase("exit")){
                                clearConsole();
                                break;
                            }
                            else if (!isValidUrl(userUrl)) {
                                clearConsole();
                                System.out.println("ERRO: Url inválido... Tente novamente ou volte atrás.\n");
                            } else{
                                clearConsole();
                                urlQueueInterface.addUrl(userUrl);
                                break;
                            }
                        }

                    }
                    default -> System.out.println("Nao encontrado. Por favor introduza outra opção.");
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para limpar a consola
    public final static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }




    // Função para verificar se um url é válido ou não:
    public static boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
