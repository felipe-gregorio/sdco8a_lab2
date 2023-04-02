
/**
 * Laboratorio 1 de Sistemas Distribuidos
 * 
 * Autor: Lucio A. Rocha
 * Ultima atualizacao: 17/12/2022
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente {

	private static Socket socket;
	private static DataInputStream entrada;
	private static DataOutputStream saida;

	private int porta = 1025;

	public void iniciar() {
		System.out.println("Cliente iniciado na porta: " + porta);

		try {

			socket = new Socket("127.0.0.1", porta);

			entrada = new DataInputStream(socket.getInputStream());
			saida = new DataOutputStream(socket.getOutputStream());

			while (true) {
				// ler requisição cliente
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String valor = (br.readLine());

				switch (valor) {
				case "read":
					saida.writeUTF("{ \"method\":\"read\",\"args\":[\"\"]");
					System.out.println(entrada.readUTF());
					break;
				case "write":
					switch (br.readLine()) {
					case "1":
						saida.writeUTF(
								"{ \"method\":\"write\",\"args\":[\"As Leis e as salsichas, é melhor não saber como são feitas.\"]");
						break;
					case "2":
						saida.writeUTF("{ \"method\":\"write\",\"args\":[\"É preciso ser Napoleão de alguma coisa\"]");
						break;
					case "3":
						saida.writeUTF(
								"{ \"method\":\"write\",\"args\":[\"Antes de começar o trabalho de mudar o mundo, dê três voltas dentro de sua casa.\"]");
						break;
					case "4":
						saida.writeUTF(
								"{ \"method\":\"write\",\"args\":[\"Fevereiro tem 28 dias. É o mês em que as mulheres falam menos.\"]");
						break;
					default:
						saida.writeUTF("{ \"method\":\"write\",\"args\":[\"É tolo quem confia no futuro\"]");
						break;
					}
					System.out.println(entrada.readUTF());
				default:
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Cliente().iniciar();
	}

}
