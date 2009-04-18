package ui.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;

import middleware.Middleware;
import middleware.Tools;
import ui.gui.menubar.TrayMenu;
import download.HosterEnum;

public class TrayIcon extends java.awt.TrayIcon {
	
	public final String toolTip = "Paste 'n' Forget";
	
	public TrayIcon() {
		super(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/icon.png").getImage());
		this.setImageAutoSize(true);
		this.setToolTip(this.toolTip);
		this.addMouseMotionListener(new TrayIconMouseListener(this));
		this.setPopupMenu(new TrayMenu());
		
	}
	
	private class TrayIconMouseListener implements MouseMotionListener {
		
		private TrayIcon trayIcon = null;
		
		public TrayIconMouseListener(TrayIcon trayIcon) {
			this.trayIcon = trayIcon;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			int downloads = 0;
			for (HosterEnum hoster : HosterEnum.values()) {
				downloads = downloads + Middleware.getQueue(hoster.getName()).getDownloadList().size();
			}
			this.trayIcon.setToolTip(this.trayIcon.toolTip + " - Downloads: " + downloads);
		}

		

		
	}

}
