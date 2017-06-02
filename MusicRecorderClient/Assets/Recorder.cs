using UnityEngine;

public class Recorder : MonoBehaviour {

	private string microphone;
	private AudioSource recording;

	void Start () {
		microphone = Microphone.devices[0];		
		recording = GetComponent<AudioSource>();
	}	

	public void StartRecording() {
		recording.clip = Microphone.Start(microphone, false, 300, 44100);
	}

	public void StopRecording() {
		Microphone.End(microphone);
		SavWav.Save ("recording.wav", recording.clip);
	}

	public void ReplayRecording() {
		recording.Play();
	}
}
