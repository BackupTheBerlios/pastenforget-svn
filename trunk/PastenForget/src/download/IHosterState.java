package download;

import download.Download;


public interface IHosterState {

	public void fireEvent(Download download);
	public String getPattern();

}
