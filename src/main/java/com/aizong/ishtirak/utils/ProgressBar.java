package com.aizong.ishtirak.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class ProgressBar<T> extends JPanel implements PropertyChangeListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6652115430113181917L;

	private JProgressBar progressBar;

	private Task task;

	private JDialog dialog;

	class Task extends SwingWorker<Return<T>, Void>
	{
		/*
		 * Main task. Executed in background thread.
		 */

		ProgressBarListener<T> listener;

		public Task(ProgressBarListener<T> listener)
		{
			if (listener == null)
			{
				throw new IllegalArgumentException("Task should have listener");
			}

			this.listener = listener;
		}

		@Override
		public Return<T> doInBackground( )
		{
			Return<T> response = new Return<T>();
			
			try
			{
				response.value = (T) listener.onBackground();
				response.response = true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return response;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done( )
		{
			try
			{
				Toolkit.getDefaultToolkit().beep();
				setCursor(null); // turn off the wait cursor
				dialog.dispose();
				if (get().isSuccess())
				{
					listener.onDone(get().value);
				}

			}
			catch (InterruptedException ex)
			{
				Logger.getLogger(ProgressBar.class.getName()).log(Level.SEVERE, null, ex);
			}
			catch (ExecutionException ex)
			{
				Logger.getLogger(ProgressBar.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}
	
	@SuppressWarnings({
	"hiding"
	})
	private class Return<T>
	{
		T value;
		Boolean response = false;
		
		public boolean isSuccess()
		{
			return response;
		}
	}

	public interface ProgressBarListener<T>
	{

		T onBackground( ) throws Exception;

		void onDone(T response );
	}

	private ProgressBar( )
	{
		super(new BorderLayout());

		progressBar = new JProgressBar(0, 100);
		// progressBar.setValue(0);
		// progressBar.setStringPainted(true);

		JPanel panel = new JPanel();
		panel.add(progressBar);

		add(panel, BorderLayout.PAGE_START);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	}

	private void start(ProgressBarListener<T> listener)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		task = new Task(listener);
		task.addPropertyChangeListener(this);
		task.execute();
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		if ("progress" == evt.getPropertyName())
		{
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

	/**
	 * Create the GUI and show it. As with all GUI code, this must run on the
	 * event-dispatching thread.
	 */
	public void showFrame(Window owner)
	{
		dialog = new JDialog(owner);
		dialog.setTitle("Loading...");
		dialog.setContentPane(this);
		dialog.pack();
		dialog.setMinimumSize(dialog.getPreferredSize());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);

	}

	public static <T> void execute(ProgressBarListener<T> listener, Window owner)
	{

		ProgressBar<T> progressBarDemo = new ProgressBar<T>();
		progressBarDemo.start(listener);
		progressBarDemo.showFrame(owner);

	}

	public static <T> void execute(ProgressBarListener<T> listener, Component component)
	{

		execute(listener,SwingUtilities.getWindowAncestor(component));

	}
}
