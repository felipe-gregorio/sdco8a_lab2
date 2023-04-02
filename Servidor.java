import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Servidor {

	private static Socket socket;
	private static ServerSocket server;

	private static DataInputStream entrada;
	private static DataOutputStream saida;

	private int porta = 1025;

	public final static Path path = Paths.get("src\\fortune-br.txt");
	private int NUM_FORTUNES = 0;

	public class FileReader {

	public int countFortunes() throws FileNotFoundException {

			int lineCount = 0;

			InputStream is = new BufferedInputStream(new FileInputStream(path.toString()));
			
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				String line = "";
				while (!(line == null)) {

					if (line.equals("%"))
						lineCount++;

					line = br.readLine();

				}// fim while

				System.out.println(lineCount);
			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
			return lineCount;
		}
	
	public void parser(HashMap<Integer, String> hm)
			throws FileNotFoundException {

		InputStream is = new BufferedInputStream(new FileInputStream(
				path.toString()));
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				is))) {

			int lineCount = 0;

			String line = "";
			while (!(line == null)) {

				if (line.equals("%"))
					lineCount++;

				line = br.readLine();
				StringBuffer fortune = new StringBuffer();
				while (!(line == null) && !line.equals("%")) {
					fortune.append(line + "\n");
					line = br.readLine();
				}

				hm.put(lineCount, fortune.toString());
				
			}

		} catch (IOException e) {
			System.out.println("SHOW: Excecao na leitura do arquivo.");
		}
	}
	
	public int getRandomIndex(HashMap<Integer, String> hm) {
        List<Integer> keys = new ArrayList<Integer>(hm.keySet());
        return new SecureRandom().nextInt(keys.size());
    }
	
	 public String read(HashMap<Integer, String> hm) throws FileNotFoundException {
	        String fortune = hm.get(getRandomIndex(hm));
	        System.out.println(fortune);
	        return fortune;
	    }
	 
	   public void write(HashMap<Integer, String> hm) throws FileNotFoundException {
	        String fortune = hm.get(getRandomIndex(hm));
	        try (PrintWriter out = new PrintWriter(path.toString())) {
	        	out.println("\n %"+ fortune);
	        }
	    }
	}
	

	public String metodo(String tmp) {
		String m = "";
		for(int i= tmp.indexOf("method:")+8; i < tmp.length() && tmp.charAt(i)!= '"' ;i++) {
			m = m + tmp.charAt(i); 
		}
		return m;
	}

	public void iniciar() {
		System.out.println("Servidor iniciado na porta: " + porta);
		try {
			FileReader fr= new FileReader();
			HashMap<Integer, String> hm = new HashMap<>();
			fr.parser(hm);
			server = new ServerSocket(porta);
			socket = server.accept();  //Processo fica bloqueado, ah espera de conexoes
			entrada = new DataInputStream(socket.getInputStream());
			saida = new DataOutputStream(socket.getOutputStream());
			String leitura;
			
			while(true) {
				
				String valor = entrada.readUTF();
				System.out.println(valor);
				switch(metodo(valor)) {
					case "read":
						leitura = fr.read(hm);
						saida.writeUTF("{ result: " + leitura);
						break;
					case "write":
						fr.write(hm);
						saida.writeUTF("{ result: Escrito");
						break;
					default:
						break;
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		new Servidor().iniciar();

	}

}
