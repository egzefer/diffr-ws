package com.egzefer.diffr.service;

import com.egzefer.diffr.model.Diff;
import com.egzefer.diffr.model.DiffDetails;
import com.egzefer.diffr.model.Side;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DiffService {

	private Set<Diff> diffs = new HashSet<>();

	public Diff addDiff(String id, Side side, String content) {
		Diff diff = new Diff(id, side, content);

		diffs.remove(diff);
		diffs.add(diff);

		return diff;
	}

	public String getDiffContent(String id, Side side) {
		Diff diff =
			diffs.stream()
				.filter(d -> d.getId().equals(id) && d.getSide().equals(side))
				.findFirst()
				.orElseThrow(NoSuchElementException::new);

		return diff.getContent();
	}

	public String decodeJsonContent(String content) {
		return new String(Base64.getDecoder().decode(content));
	}

	/** Used for testing */
	Set<Diff> getDiffs() {
		return diffs;
	}

	public DiffDetails compare(String id) {
		DiffDetails details = new DiffDetails(id, false, false);

		Diff leftDiff = getDiffBy(id, Side.left);

		Diff rightDiff = getDiffBy(id, Side.right);

		if (leftDiff == null && rightDiff == null) {
			details.setMessage("Unable to compare. Both left and right sides are null");
			return details;
		} 
		
		if (leftDiff == null) {
			details.setMessage("Unable to compare. Left side is null");
			return details;
		}

		if (rightDiff == null) {
			details.setMessage("Unable to compare. Right side is null");
			return details;
		}

		return compareContents(details, leftDiff.getContent(), rightDiff.getContent());
	}

	private DiffDetails compareContents(DiffDetails details, String leftContent, String rightContent) {
		if (leftContent.length() != rightContent.length()) {
			details.setMessage("Contents have different size");
			return details;
		}

		details.setSameSize(true);

		if (leftContent.equals(rightContent)) {
			details.setEqual(true);
			details.setMessage("Both contents are equal");
		} else {
			details.setMessage(calculateDifferences(leftContent, rightContent));
		}

		return details;
	}

	private String calculateDifferences(String leftContent, String rightContent) {
		Integer diffLength = 0;
		Integer startIndex = -1;

		List<String> diffList = new ArrayList<>();
		diffList.add("Differences found at the following offset(s):");

		for (int i = 0; i < leftContent.length(); i++) {
			if (leftContent.charAt(i) != rightContent.charAt(i)) {
				if (startIndex == -1) {
					startIndex = i;
				}
				diffLength += 1;
			} else {
				if (startIndex > 0) {
					diffList.add(String.format(" offset: %s, length: %s", startIndex, diffLength));
					diffLength = 0;
					startIndex = -1;
				}
			}
		}

		return diffList.stream().collect(Collectors.joining("\n"));
	}

	private Diff getDiffBy(String id, Side side) {
		return diffs.stream()
			.filter(d -> d.getId().equals(id) && d.getSide().equals(side))
			.findFirst()
			.orElse(null);
	}

}
