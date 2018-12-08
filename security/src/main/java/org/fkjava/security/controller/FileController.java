package org.fkjava.security.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.fkjava.security.domain.FileInfo222;
import org.fkjava.security.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
@RequestMapping("/security/list")
public class FileController {

	@Autowired
	private FileService fileService;	
	
	
	@PostMapping
	public String upload(@RequestParam("file") MultipartFile file) throws IOException {
		String name = file.getOriginalFilename();
		String contentType = file.getContentType();
		long fileSize = file.getSize();
		
		//流里面就是文件内容
		try(
				InputStream in = file.getInputStream()){
				fileService.save(name,contentType,fileSize,in); 
		}
		return "redirect:/security/list";
	}
	
	@GetMapping
	public ModelAndView list(
			@RequestParam(name="keyword",required=false)String keyword,
			@RequestParam(name="pageNumber",defaultValue="0")Integer number) {
		Page<FileInfo222> page = this.fileService.findFiles(keyword,number);
		ModelAndView mav  = new ModelAndView("security/list");
		
		mav.addObject("page",page);
		
		return mav;
	}
	
	@GetMapping("/{abc}")
	public ResponseEntity<StreamingResponseBody> list(@PathVariable("abc")String id,
			// 获取请求头
			@RequestHeader("User-Agent") String userAgent) throws FileNotFoundException{
		System.out.println("异步请求+--------------");
		//读取文件信息
		FileInfo222 fi = this.fileService.findById(id);
		//读取文件内容
		InputStream in = this.fileService.getFileContent(fi);
		
		//构建响应体
		BodyBuilder builder = ResponseEntity.ok(); //HTTP 200
		builder.contentLength(fi.getFileSize());// 读取文件内容
		// MediaType.valueOf把字符串转换为MediaType
		builder.contentType(MediaType.valueOf(fi.getContentType()));// 文件类型
		
		String name = fi.getName();
		// 对文件名进行编码，避免出现文件名乱码的问题
		name = URLEncoder.encode(name, Charset.forName("UTF-8"));
		builder.header("Content-Disposition", "attachment*=UTF-8''" + name);
		
		System.out.println("用户的浏览器: " + userAgent);

		System.out.println("当前线程ID: " + Thread.currentThread().getId());
		
		StreamingResponseBody body = new StreamingResponseBody() {
			
			@Override
			public void writeTo(OutputStream out) throws IOException {
				System.out.println("处理文件内容的线程ID: " + Thread.currentThread().getId());
				// 当把StreamingResponseBody作为响应体的时候，Spring MVC就会开启异步Servlet的请求
				// 此时相当于在这里面写代码，是在另外一个线程执行的
				try(in){
					byte[] b = new byte[2048];
					for(int len = in.read(b);
							len != -1;
							len = in.read(b)
									) {
						
						out.write(b, 0, len);
		}
		}
		}
		};
		
		ResponseEntity<StreamingResponseBody> entity = builder.body(body);
		return entity;
	}
}
