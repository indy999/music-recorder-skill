using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Connection : MonoBehaviour, ClientHandler {

	enum State{
		CONNECTED,
		CONNECTING,
		DISCONNECTED
	};

	Client client;
	State state;
	public Recorder recorder;

	void Start () {
		client = new Client (this);
		state = State.DISCONNECTED;
	}

	void Update () {
		if (state == State.DISCONNECTED) {
			Connect ();
		}
		client.Update ();
	}

	public void Connect(){
		Debug.Log ("Connecting...");
		state = State.CONNECTING;
		client.Login ("ec2-52-212-215-99.eu-west-1.compute.amazonaws.com", 8080, "58", "Mash", "Casey", "");
	}

	#region ClientHandler implementation

	public void OnLogin (bool result, string userId, string message)
	{		
		if (result) {
			Debug.Log ("Login Successful!");
			state = State.CONNECTED;
		} else {
			state = State.DISCONNECTED;
		}
	}

	public void OnLogout (string message)
	{
		Debug.Log ("Login Failed!" + message);
		state = State.DISCONNECTED;
	}

	public void OnRoomFound (int roomId, string name, Packets.ClientInfo[] userIds)
	{
		throw new System.NotImplementedException ();
	}

	public void OnClientLeft (string userId)
	{
		throw new System.NotImplementedException ();
	}

	public void OnError (string errorMsg)
	{
		throw new System.NotImplementedException ();
	}

	public void OnMessage (string msgType, string json)
	{
		Debug.Log ("Message received!" + json);
	}

	public void OnNotification(string msgType, string json)
	{
		Debug.Log ("Notification received!" + json);
		Packets.Intent intent = JsonUtility.FromJson<Packets.Intent> (json);
		if (intent.name == "StartRecording") {
			recorder.StartRecording ();	
		} else if (intent.name == "StopRecording") {
			recorder.StopRecording ();
		} else if (intent.name == "PlaybackRecording") {
			recorder.ReplayRecording ();
		}
	}

	#endregion
}
