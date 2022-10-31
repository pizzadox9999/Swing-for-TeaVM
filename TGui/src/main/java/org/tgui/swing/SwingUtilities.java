package org.tgui.swing;

import org.teavm.jso.browser.AnimationFrameCallback;
import org.teavm.jso.browser.Window;

public class SwingUtilities implements SwingConstants {
    private SwingUtilities() {
        // do nothing
    }

    public static void invokeLater(Runnable doRun) {
    	Window.current().requestAnimationFrame(new AnimationFrameCallback() {
			public void onAnimationFrame(double timestamp) {
				doRun.run();
			}
		});
    }

    public static void invokeAndWait(Runnable doRun) {
        doRun.run();
    }
}
