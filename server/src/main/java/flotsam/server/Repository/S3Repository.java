package flotsam.server.Repository;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Repository
public class S3Repository {
    @Autowired
	private AmazonS3 s3;

	public String S3Upload(String contentType, InputStream is) {
		String id = UUID.randomUUID().toString().substring(0, 8);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(contentType);

		PutObjectRequest putReq = new PutObjectRequest("thisisabucket", id, is, metadata);
		putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);

		s3.putObject(putReq);

		return id;
	}

	public String getURL(String id) {
		System.out.println(s3.getUrl("thisisabucket", id).toExternalForm());
		return s3.getUrl("thisisabucket", id).toExternalForm();
		
	}

	
}
