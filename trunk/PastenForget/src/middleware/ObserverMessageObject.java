package middleware;

import java.awt.Image;

import queue.Queue;
import download.Download;

/**
 * Dieses Object dient zur Verstaendigung zwischen Beobachter und Subjekten.
 * 
 * @author executor
 *
 */

public class ObserverMessageObject {
	
	private boolean isQueue = false;
	
	private boolean isDownload = false;
	
	private Queue queue = null;
	
	private Download download = null;
	
	private boolean wantCaptcha = false;
	
	private Image captcha = null;

	public boolean isQueue() {
		return isQueue;
	}

	public void setQueue(boolean isQueue) {
		this.isQueue = isQueue;
	}

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
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

	public boolean isWantCaptcha() {
		return wantCaptcha;
	}

	public void setWantCaptcha(boolean wantCaptcha) {
		this.wantCaptcha = wantCaptcha;
	}

	public Image getCaptcha() {
		return captcha;
	}

	public void setCaptcha(Image captcha) {
		this.captcha = captcha;
	}

}
