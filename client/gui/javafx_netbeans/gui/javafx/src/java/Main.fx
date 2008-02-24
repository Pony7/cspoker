import javafx.ui.*;
import java.lang.*;
import Login;
import org.client.ClientCore;

class Main{
		attribute client: ClientCore;
		attribute login: Login;
}
var prog= Main{
		client: new ClientCore()    
		login: Login
}; 
