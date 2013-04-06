package test;
import util.Trade;

import com.rsbuddy.script.ActiveScript;
import com.rsbuddy.script.Manifest;

@Manifest(authors={"Kieve"}, name="UI creation script", keywords={"Testing"}, version=1337, description="Creating my layout.")
public class Test extends ActiveScript {
	public int loop() {
		if(Trade.isOpen()){
			log(Trade.offeringContainsAll(1139,526));
		}
		sleep(800);
		return 0;
	}
}
