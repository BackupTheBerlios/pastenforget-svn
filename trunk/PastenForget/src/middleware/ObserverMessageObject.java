package middleware;

import java.awt.Image;

import download.Download;

import queue.Queue;

/**
 * Dieses Object dient zur Verstaendigung zwischen Beobachter und Subjekten.
 * 
 * @author executor
 * 
 */

public class ObserverMessageObject {

	private Queue queue = null;

	private Download download = null;

	private Image captcha = null;

	public boolean isQueue() {
		if (this.queue != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public ObserverMessageObject (Queue queue) {
		this.queue = queue;
	}
	
	public ObserverMessageObject (Download download) {
		this.download = download;
	}
	
	public ObserverMessageObject (Download download, boolean captcha) {
		this.download = download;
		if (captcha) {
			this.captcha = download.getCaptcha();
		}		
	}

	public boolean isDownload() {
		if (this.download != null) {
			return true;
		} else {
			return false;
		}
	}

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public Download getDownload() {
		return download;
	}

	public void setDownload(Download download) {
		this.download = download;
	}

	public boolean isCaptcha() {
		if (this.captcha != null) {
			return true;
		} else {
			return false;
		}
	}

	public Image getCaptcha() {
		return captcha;
	}

	public void setCaptcha(Image captcha) {
		this.captcha = captcha;
	}

}
