package com.egzefer.diffr.service;

import com.egzefer.diffr.model.Diff;
import com.egzefer.diffr.model.DiffDetails;
import com.egzefer.diffr.model.Side;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class DiffServiceTest {

	private static final String ID_1 = "1";
	private static final String JOHN_DOE_JSON_ENCODED = "ewoJImZpcnN0TmFtZSI6IkpvaG4iLAoJImxhc3ROYW1lIjoiRG9lIiwKCSJjb2RlIjoiMDAwMSIKfQ==";
	private static final String JOSH_DOE_JSON_ENCODED = "ewoJImZpcnN0TmFtZSI6Ikpvc2giLAoJImxhc3ROYW1lIjoiRG9lIiwKCSJjb2RlIjoiMDAwMiIKfQ==";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@InjectMocks
	private DiffService diffService;

	@Test
	public void addDiff_validDiffEmptySet_oneDiffAdded() {
		diffService.addDiff(ID_1, Side.left, JOHN_DOE_JSON_ENCODED);

		Assert.assertEquals("One Diff object added",1, diffService.getDiffs().size());

		Diff diff = diffService.getDiffs().iterator().next();
		Assert.assertEquals(ID_1, diff.getId());
		Assert.assertEquals(Side.left, diff.getSide());
		Assert.assertEquals(JOHN_DOE_JSON_ENCODED, diff.getContent());
	}

	@Test
	public void addDiff_twoDiffsSameIdSameSide_oneDiffAdded() {
		diffService.addDiff(ID_1, Side.left, JOHN_DOE_JSON_ENCODED);
		diffService.addDiff(ID_1, Side.left, JOSH_DOE_JSON_ENCODED);

		Assert.assertEquals("One Diff object added",1, diffService.getDiffs().size());

		Diff diff = diffService.getDiffs().iterator().next();
		Assert.assertEquals(ID_1, diff.getId());
		Assert.assertEquals(Side.left, diff.getSide());
		Assert.assertEquals(JOSH_DOE_JSON_ENCODED, diff.getContent());
	}

	@Test
	public void addDiff_twoDiffsSameIdDifferentSide_twoDiffsAdded() {
		diffService.addDiff(ID_1, Side.left, JOHN_DOE_JSON_ENCODED);
		diffService.addDiff(ID_1, Side.right, JOSH_DOE_JSON_ENCODED);

		Assert.assertEquals("Two Diff objects added",2, diffService.getDiffs().size());
	}

	@Test
	public void getDiffContent_diffFound_diffContentReturned() {
		diffService.addDiff(ID_1, Side.left, JOHN_DOE_JSON_ENCODED);

		String diffContent = diffService.getDiffContent(ID_1, Side.left);

		Assert.assertEquals(JOHN_DOE_JSON_ENCODED, diffContent);
	}

	@Test
	public void getDiffContent_diffNotFound_exceptionThrown() {
		expectedException.expect(NoSuchElementException.class);

		diffService.getDiffContent(ID_1, Side.left);
	}

	@Test
	public void decodeJsonContent_encodedContentGiven_contentDecoded() {
		String decodedJsonContent = diffService.decodeJsonContent(JOHN_DOE_JSON_ENCODED);

		Assert.assertEquals(
			"{\n\t\"firstName\":\"John\",\n\t\"lastName\":\"Doe\",\n\t\"code\":\"0001\"\n}", decodedJsonContent);
	}

	@Test
	public void compare_equalContents_equalContentsMessageResponse() {
		diffService.addDiff(ID_1, Side.left, JOHN_DOE_JSON_ENCODED);
		diffService.addDiff(ID_1, Side.right, JOHN_DOE_JSON_ENCODED);

		DiffDetails diffDetails = diffService.compare(ID_1);

		Assert.assertEquals(ID_1, diffDetails.getId());
		Assert.assertTrue(diffDetails.isEqual());
		Assert.assertTrue(diffDetails.isSameSize());
		Assert.assertEquals("Both contents are equal", diffDetails.getMessage());
	}

	@Test
	public void compare_differentSizeContent_differentSizeContentsMessageResponse() {
		diffService.addDiff(ID_1, Side.left, JOHN_DOE_JSON_ENCODED);
		diffService.addDiff(ID_1, Side.right, JOSH_DOE_JSON_ENCODED + "abc123");

		DiffDetails diffDetails = diffService.compare(ID_1);

		Assert.assertEquals(ID_1, diffDetails.getId());
		Assert.assertFalse(diffDetails.isEqual());
		Assert.assertFalse(diffDetails.isSameSize());
		Assert.assertEquals("Contents have different size", diffDetails.getMessage());
	}

	@Test
	public void compare_sameSizeContents_diffOffsetDetailsMessageResponse() {
		diffService.addDiff(ID_1, Side.left, JOHN_DOE_JSON_ENCODED);
		diffService.addDiff(ID_1, Side.right, JOSH_DOE_JSON_ENCODED);

		DiffDetails diffDetails = diffService.compare(ID_1);

		Assert.assertEquals(ID_1, diffDetails.getId());
		Assert.assertFalse(diffDetails.isEqual());
		Assert.assertTrue(diffDetails.isSameSize());
		Assert.assertEquals(
			"Differences found at the following offset(s):\n offset: 24, length: 3\n offset: 73, length: 1",
			diffDetails.getMessage());
	}

	@Test
	public void compare_bothContentNull_bothContentNullMessageResponse() {
		DiffDetails diffDetails = diffService.compare(ID_1);

		Assert.assertEquals(ID_1, diffDetails.getId());
		Assert.assertFalse(diffDetails.isEqual());
		Assert.assertFalse(diffDetails.isSameSize());
		Assert.assertEquals("Unable to compare. Both left and right sides are null", diffDetails.getMessage());
	}

	@Test
	public void compare_leftContentNull_leftContentNullMessageResponse() {
		diffService.addDiff(ID_1, Side.right, JOHN_DOE_JSON_ENCODED);

		DiffDetails diffDetails = diffService.compare(ID_1);

		Assert.assertEquals(ID_1, diffDetails.getId());
		Assert.assertFalse(diffDetails.isEqual());
		Assert.assertFalse(diffDetails.isSameSize());
		Assert.assertEquals("Unable to compare. Left side is null", diffDetails.getMessage());
	}

	@Test
	public void compare_rightContentNull_rightContentNullMessageResponse() {
		diffService.addDiff(ID_1, Side.left, JOHN_DOE_JSON_ENCODED);

		DiffDetails diffDetails = diffService.compare(ID_1);

		Assert.assertEquals(ID_1, diffDetails.getId());
		Assert.assertFalse(diffDetails.isEqual());
		Assert.assertFalse(diffDetails.isSameSize());
		Assert.assertEquals("Unable to compare. Right side is null", diffDetails.getMessage());
	}

}