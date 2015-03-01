package controller.menu;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public interface MenuNavigationI {
	public void pushMenu(MenuI menu);

	public void popMenu(MenuI menu);

	public void quit();
}
