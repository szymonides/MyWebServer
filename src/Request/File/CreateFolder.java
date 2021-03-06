package Request.File;

import com.bmarius.sockets.WebSocketClient;
import entities.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import mywebsocket.JobToDo;

/**
 *
 * @author Szymon Skrzyński <skrzynski.szymon@gmail.com>
 */
public class CreateFolder extends JobToDo {
    
    public CreateFolder(WebSocketClient client) {
        super(client, "File", "CreateFolder");
    }
    
    public void run() {
        System.out.println(this.data.toString());
        
        if (this._validateData() == false) {
            this._sendResponseInvalidData();
            return;
        }
        
        Long parentId = (Long) this.data.get("parentId");
        String name = (String) this.data.get("name");
        
        File parentFolder = (File) this._session.get(File.class, parentId);
        
        File folder = new File();
        folder.setName(name);
        folder.setParentId(parentFolder);
        folder.setCreatedAt(new Date());
        folder.setMimeType(null);
        
        Long newFolderId = (Long) this._session.save(folder);
        this._session.getTransaction().commit();
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.data.put("setCreatedAt", dateFormat.format(folder.getCreatedAt()));
        this.data.put("id", newFolderId);
        
        this._response.data = this.data;
        this._sendToAllClients();
    }
    
    private boolean _validateData() {
        String name = (String) this.data.get("name");
        if (name == null) {
            return false;
        }
        
        if (this.data.get("parentId") == null) {
            return false;
        }
        
        Long parentId = (Long) this.data.get("parentId");
        return this._validateFolderExist(parentId);
    }
    
    private boolean _validateFolderExist(Long folderId) {
        this._session.beginTransaction();
        File folder = (File) this._session.get(File.class, folderId);
        
        
        if (folder == null) {
            return false;
        }
        
        if (folder.getMimeType() != null) {
            return false;
        }
        
        return true;
    }
}