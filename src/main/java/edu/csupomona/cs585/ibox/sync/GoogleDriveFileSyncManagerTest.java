package edu.csupomona.cs585.ibox.sync;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Delete;
import com.google.api.services.drive.Drive.Files.Insert;
import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.Drive.Files.Update;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;


public class GoogleDriveFileSyncManagerTest {
	
	GoogleDriveFileSyncManager MockGoogleDrive;
	Drive MockDriveService;
	
	java.io.File localFile;
	File body;
	Files getFiles;
	List getList;
	FileList listOfFiles;
	java.util.List<File> filesList;
	

	Insert insert;
	Update update;
	Delete delete;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);	
		
		MockDriveService = mock(Drive.class);
		MockGoogleDrive = new GoogleDriveFileSyncManager(MockDriveService);
		
		localFile = mock(java.io.File.class);
		body = new File();
		listOfFiles = new FileList();
		getFiles = mock(Files.class);
		getList = mock(List.class);
		filesList = new ArrayList<File>();
		
		insert = mock(Insert.class);
		update = mock(Update.class);
		delete = mock(Delete.class);
		
	}
	
	@After
	public void tearDown() throws Exception {
		//set all objects to null after completion
		MockDriveService = null;
		MockGoogleDrive = null;
		localFile = null;
		body = null;
		listOfFiles = null;
		getFiles = null;
		getList = null;
		filesList = null;
		insert = null;
		update = null;
		delete = null;
	}
	
	public void initalize(){
		File testFile = new File();
		testFile.setId("TestFile");
		testFile.setTitle("test.txt");
		
		filesList.add(testFile);
		listOfFiles.setItems(filesList);
	}
	
	@Test
	public void testAddFile() throws IOException {
		//Stubbing
		when(MockDriveService.files()).thenReturn(getFiles);
		when(getFiles.insert(isA(File.class), isA(AbstractInputStreamContent.class))).thenReturn(insert);
		when(insert.execute()).thenReturn(body);
		
		//Method Call
		MockGoogleDrive.addFile(localFile);		
		
		//Verify
		Mockito.verify(MockDriveService, atLeast(0)).files();
	    Mockito.verify(getFiles, atLeast(0)).insert(isA(File.class));
	    Mockito.verify(insert, atLeast(0)).execute();		
	}

	@Test
	public void testUpdateFile() throws IOException{
		
		initalize();
		
		//Stubbing
		when(localFile.getName()).thenReturn("test.txt");
		when(MockDriveService.files()).thenReturn(getFiles);
		when(getFiles.list()).thenReturn(getList);
		when(getList.execute()).thenReturn(listOfFiles);
		
		String fileId = MockGoogleDrive.getFileId("test.txt");
		
		when(getFiles.update(eq(fileId),isA(File.class), isA(AbstractInputStreamContent.class))).thenReturn(update);
		when(update.execute()).thenReturn(body);
		
		//Method Call
		MockGoogleDrive.updateFile(localFile);
	
		//Verifying
		verify(MockDriveService, atLeast(0)).files();
	    verify(getFiles, atLeast(0)).update(eq(fileId), Mockito.isA(File.class));
	    verify(update, atLeast(0)).execute();
	
	}
		
	@Test
	public void testDeleteFile() throws IOException {
		initalize();
		
		//Stubbing
		when(localFile.getName()).thenReturn("test.txt");
		when(MockDriveService.files()).thenReturn(getFiles);
		when(getFiles.list()).thenReturn(getList);
		when(getList.execute()).thenReturn(listOfFiles);
		
		String fileId = MockGoogleDrive.getFileId("test.txt");
		
		when(getFiles.delete(eq(fileId))).thenReturn(delete);
		when(delete.execute()).thenReturn(null);
		
		//Method call
		MockGoogleDrive.deleteFile(localFile);
		
		//Verifying
		verify(MockDriveService, atLeastOnce()).files();
	    verify(getFiles, atLeast(0)).delete(eq(fileId));
	    verify(delete, atLeastOnce()).execute();		
	
	}

}
