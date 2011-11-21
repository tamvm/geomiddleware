package com.gisgraphy.hibernate.criterion;

import org.hibernate.criterion.SimpleProjection;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public abstract class MySimpleProjection extends SimpleProjection {
	private static final long serialVersionUID = 2544573449234456138L;
	private int position;

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
}
