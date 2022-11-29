package com.shopping.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
public class FileService {
    // 상품에 대한 이미지 파일을 업로드해주는 메소드입니다.
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        // uploadPath) 이미지 업로드 경로
        // originalFileName) 업로드될 이미지의 원본 이름
        // fileData) 업로드할 바이트 데이터
        UUID uuid = UUID.randomUUID() ; //  unique identifier

        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")) ; // 파일 확장자

        String savedFileName = uuid.toString() + extension ; // 저장될 파일 이름
        String fileUploadFullUrl = uploadPath + "/" + savedFileName ; // 전체 경로 포함

        System.out.println("저장될 파일 이름 : " + savedFileName);
        System.out.println("전체 경로 포함 : " + fileUploadFullUrl);

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl) ;
        fos.write(fileData); // 네트워크를 통한 파일 쓰기(업로드)
        fos.close();

        return savedFileName ;
    }

    // 상품 이미지에 대한 수정 작업이 이루어질때, 기존 이미지를 삭제하는 메소드입니다.
    public void deleteFile(String filePath) throws Exception{
        // filePath) 삭제될 파일의 경로와 이름 정보를 담고 있는 변수
        File deleteFile = new File(filePath) ; // 삭제할 파일 객체

        if (deleteFile.exists()) { // 파일이 존재하면 삭제하기
            deleteFile.delete() ;
            System.out.println(filePath + " 파일을 삭제 하였습니다.");
        }else{
            System.out.println(filePath + " 파일이 존재하지 않습니다.");
        }
    }
}
