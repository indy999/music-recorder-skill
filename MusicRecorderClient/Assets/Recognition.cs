using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Windows.Speech;
using System.Linq;

public class Recognition : MonoBehaviour {

	KeywordRecognizer keywordRecogniser;
	Dictionary<string, System.Action> keywords = new Dictionary<string, System.Action>();

	void Start() {		
		keywords.Add ("alexa", () => {
			OnAlexa();	
		});

		keywordRecogniser = new KeywordRecognizer (keywords.Keys.ToArray());
		keywordRecogniser.OnPhraseRecognized += KeywordRecogniserOnPhraseRecognised;
		keywordRecogniser.Start ();
	}

	void KeywordRecogniserOnPhraseRecognised(PhraseRecognizedEventArgs args){
		System.Action keywordAction;
		if (keywords.TryGetValue (args.text, out keywordAction)) {
			keywordAction.Invoke ();
		}
	}


	void OnAlexa() {
		Debug.Log ("ALEXA!");
	}
}
