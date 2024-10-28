package com.pgsanchez.ww2dates.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	private final Path rootLocation = Paths.get("uploadDir");
	
	public String store(MultipartFile file) throws Exception {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		String extension = StringUtils.getFilenameExtension(filename);
		String storedFilename = System.currentTimeMillis() + "." + extension;
		
		if (file.isEmpty()) {
			throw new Exception("Archivo enviado vacío");
		}
		if (filename.contains("..")) {
			throw new Exception("Nombre de archivo incorrecto");
		}
		
		try (InputStream inputStream = file.getInputStream()){
			Files.copy(inputStream, this.rootLocation.resolve(storedFilename), StandardCopyOption.REPLACE_EXISTING);
			return storedFilename;
		} catch (IOException io) {
			throw new Exception("Error al almacenar el archivo");
		}
	}
	
	public boolean delete(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			if (Files.exists(file)) {
				Files.delete(file);
				return true;
			} else {
				return false;
			}
		}catch (IOException io) {
			return false;
		}
	}
	
	public Resource loadAsResource(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if(resource.exists()||resource.isReadable()) {
				return resource;
			}else {
				throw new Exception();
			}
		}catch (Exception e) {
			System.err.println("Error IO" + e.getMessage());
		}
		return null;
	}
	
	
	
}
