package com.yana.PrivateNetWorking.Node.shutDownIF;

import com.yana.PrivateNetWorking.Node.gui.GuiController;

public class InvokeShutDownEvent {
	private static InvokeShutDownEvent instance = new InvokeShutDownEvent();
	private GuiController guiController;
	private InvokeShutDownEvent() {
	}

	public static InvokeShutDownEvent getInstance() {
		return instance;
	}

	public void setGuiController(GuiController guiController) {
		this.guiController = guiController;
	}

	public void invoke() {
		if(this.guiController == null) {
			return;
		}
		this.guiController.invokeExitEvent();
	}
}
