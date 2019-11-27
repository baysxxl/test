package net.wlfeng.test.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileDTO implements Serializable {

	private String url;
	
	private String fileName;
	
	private String fileSize;
}
