import com.google.gson.*;

import org.bukkit.configuration.file.YamlConfiguration;
import okhttp3.*;

import java.io.*;
import java.nio.file.*;

public class Main {
	public static YamlConfiguration pack;
	
	public static void main(String[] args) {
		pack = YamlConfiguration.loadConfiguration(new File("pack.yml"));
		new PackBuilder(new File("packs"))
			.generate(new File(".data/output"))
		;
		/*
		try {
			uploadFile(
			Files.readString(Path.of("secret.txt")).trim(),
			"pack.yml",
			"https://lobfile.com/api/v3/upload.php"
			);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	public static void uploadFile(String apiKey, String filePath, String url) throws IOException {
		OkHttpClient client = new OkHttpClient();

		File file = new File(filePath);
		if (!file.exists()) {
			throw new IllegalArgumentException("File not found: " + filePath);
		}

		RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));
		RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("file", file.getName(), fileBody).build();
		Request request = new Request.Builder().url(url).post(requestBody).addHeader("X-API-Key", apiKey).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Upload failed: " + response.code() + " " + response.message());
			}
			System.out.println("Response: " + response.body().string());
		} finally {
			client.connectionPool().evictAll();
			client.dispatcher().executorService().shutdown();
		}
	}
}