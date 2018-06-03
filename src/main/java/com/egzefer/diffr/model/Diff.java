package com.egzefer.diffr.model;

import java.util.Base64;
import java.util.Objects;

public class Diff {

	private final String id;

	private final Side side;

	private final String content;

	public Diff(String id, Side side, String content) {
		this.id = id;
		this.side = side;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public Side getSide() {
		return side;
	}

	public String getContent() {
		return content;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Diff)) {
			return false;
		}

		Diff diff = (Diff) obj;

		String id = diff.getId();
		Side side = diff.getSide();
//		String content = diff.getContent();

		return getId().equals(id) && getSide() == side
//			&& content.equals(diff.getContent())
			;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.side);
	}

}
