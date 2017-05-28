using System;

[Serializable]
public class Message
{
	public string name;
}

[Serializable]
public class Ping : Message {
	public string message;
}

[Serializable]
public class Pong : Message {
	public string message;
}