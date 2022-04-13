package brokenLinks;

import java.io.IOException;

public class FindBrokenLinksMain {

	public static void main(String[] args) throws Exception {

		FindBrokenLinks fbl = new FindBrokenLinks();
		fbl.broswerHomePage();
		fbl.findBrokenAndNonBrokenLinks();
		fbl.brokenLinks();
		fbl.unbrokenLinks();
		fbl.ImportExcelsheet();
		fbl.fileReader();
		
	}

}
