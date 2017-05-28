using System.Net.Sockets;
using System.IO;
using System.Net;
using Newtonsoft.Json;

public interface IClientHandler {
	void OnLogin (bool result, string userId, string message);
	void OnLogout(string message);
	void OnRoomFound(int roomId, string name, Packets.ClientInfo[] userIds);
	void OnClientLeft(string userId);
	void OnError(string errorMsg);
	void OnMessage(string msgType, string json);
}

public class Client {

	private TcpClient mClient = null;
	private IClientHandler mHandler = null;
	private BinaryWriter mStreamWriter = null;
	private BinaryReader mStreamReader = null;

	public Client(IClientHandler handler){
		mClient = new TcpClient ();
        
		mHandler = handler;
	}

	public void Update () {		
		if(mStreamReader != null && 
			mClient.GetStream().DataAvailable) {			
			var size = IPAddress.NetworkToHostOrder(mStreamReader.ReadInt32 ());
			var opCode = IPAddress.NetworkToHostOrder(mStreamReader.ReadInt32());
			string payload = System.Text.Encoding.UTF8.GetString(mStreamReader.ReadBytes (size - 4));

			HandlePacket((OpCode)opCode, payload);
		}
	}

	public async  void Login (string hostname, int port, string userId, string firstName, string lastName, string pictureUrl) {		
		try {
            

            await mClient.ConnectAsync (hostname, port);
            while (!mClient.Connected)
            {
                //sleep
            }
            mStreamWriter = new BinaryWriter(mClient.GetStream());
            mStreamReader = new BinaryReader(mClient.GetStream());
            WritePacket(OpCode.Login, new Packets.Login() { userId = userId, firstName = firstName, lastName = lastName, pictureUrl = pictureUrl });
        } catch (SocketException exception) {
			mHandler.OnLogout (exception.Message);
		}
	}

	public void FindRoom(string name, int maxPlayers) {
		if (mClient.Connected) {
			Packets.FindRoom findRoom = new Packets.FindRoom ();
			findRoom.name = name;
			findRoom.maxPlayers = maxPlayers;
			WritePacket (OpCode.FindRoom, findRoom);
		}	
	}

	public void BroadcastMessage(object o){
		if (mClient.Connected) {
			WritePacket (OpCode.BroadcastMessage, o);
		}
	}

    public void SendNotification(object o)
    {
        if (mClient.Connected)
        {
            WritePacket(OpCode.Notification, o);
        }
    }
    private void WritePacket(OpCode code, object o){
		int opCode = (int)code;
		string payload = JsonConvert.SerializeObject (o);
		int size = payload.Length + 4;

		mStreamWriter.Write (IPAddress.HostToNetworkOrder (size));
		mStreamWriter.Write (IPAddress.HostToNetworkOrder (opCode));
		mStreamWriter.Write (System.Text.Encoding.UTF8.GetBytes(payload));
		mStreamWriter.Flush ();
	}

	private void HandlePacket(OpCode opCode, string payload) {	
		switch (opCode) {
		case OpCode.LoginResult:
			Packets.LoginResult loginResult = JsonConvert.DeserializeObject<Packets.LoginResult> (payload);
			mHandler.OnLogin (loginResult.status, loginResult.userId, loginResult.message);
			break;
		case OpCode.RoomFound:
			Packets.RoomFound roomFound = JsonConvert.DeserializeObject<Packets.RoomFound> (payload);
			mHandler.OnRoomFound (roomFound.roomId, roomFound.roomName, roomFound.clients);
			break;
		case OpCode.BroadcastMessage:
			Message m = JsonConvert.DeserializeObject<Message> (payload);
			mHandler.OnMessage (m.name, payload);
			break;
		case OpCode.Error:
			Packets.Error error = JsonConvert.DeserializeObject<Packets.Error> (payload);
			mHandler.OnError (error.message);
			mHandler.OnLogout (error.message);
			break;
		default:
			break;
		}

	}

}
