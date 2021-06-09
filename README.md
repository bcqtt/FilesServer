# FilesServer
一个上传文件的Demo，初衷是用于公司局域网内的文件共享和传输

# nginx配置

```
# File Server
		location /download {
			charset gbk,utf-8;
			alias F:\Download;
			allow all;
			autoindex on;
			autoindex_exact_size off;
			autoindex_localtime on;
		}
```
