package com.egzefer.diffr.service;

import com.egzefer.diffr.model.Diff;
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

	private static final String JOHN_DOE_JSON_ENCODED = "ewoJImZpcnN0TmFtZSI6IkpvaG4iLAoJImxhc3ROYW1lIjoiRG9lIiwKCSJjb2RlIjoiMDAwMSIKfQ==";
	private static final String JOSH_DOE_JSON_ENCODED = "ewoJImZpcnN0TmFtZSI6Ikpvc2giLAoJImxhc3ROYW1lIjoiRG9lIiwKCSJjb2RlIjoiMDAwMiIKfQ==";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@InjectMocks
	private DiffService diffService;

	@Test
	public void addDiff_validDiffEmptySet_oneDiffAdded() {
		diffService.addDiff("1", Side.left, JOHN_DOE_JSON_ENCODED);

		Assert.assertEquals("One Diff object added",1, diffService.getDiffs().size());

		Diff diff = diffService.getDiffs().iterator().next();
		Assert.assertEquals("1", diff.getId());
		Assert.assertEquals(Side.left, diff.getSide());
		Assert.assertEquals(JOHN_DOE_JSON_ENCODED, diff.getContent());
	}

	@Test
	public void addDiff_twoDiffsSameIdSameSide_oneDiffAdded() {
		diffService.addDiff("1", Side.left, JOHN_DOE_JSON_ENCODED);
		diffService.addDiff("1", Side.left, JOSH_DOE_JSON_ENCODED);

		Assert.assertEquals("One Diff object added",1, diffService.getDiffs().size());

		Diff diff = diffService.getDiffs().iterator().next();
		Assert.assertEquals("1", diff.getId());
		Assert.assertEquals(Side.left, diff.getSide());
		Assert.assertEquals(JOSH_DOE_JSON_ENCODED, diff.getContent());
	}

	@Test
	public void addDiff_twoDiffsSameIdDifferentSide_twoDiffsAdded() {
		diffService.addDiff("1", Side.left, JOHN_DOE_JSON_ENCODED);
		diffService.addDiff("1", Side.right, JOSH_DOE_JSON_ENCODED);

		Assert.assertEquals("Two Diff objects added",2, diffService.getDiffs().size());
	}

	@Test
	public void getDiffContent_diffFound_diffContentReturned() {
		diffService.addDiff("1", Side.left, JOHN_DOE_JSON_ENCODED);

		String diffContent = diffService.getDiffContent("1", Side.left);

		Assert.assertEquals(JOHN_DOE_JSON_ENCODED, diffContent);
	}

	@Test
	public void getDiffContent_diffNotFound_exceptionThrown() {
		expectedException.expect(NoSuchElementException.class);

		diffService.getDiffContent("1", Side.left);
	}

	@Test
	public void decodeJsonContent_encodedContentGiven_contentDecoded() {
		String decodedJsonContent = diffService.decodeJsonContent(JOHN_DOE_JSON_ENCODED);

		Assert.assertEquals(
			"{\n\t\"firstName\":\"John\",\n\t\"lastName\":\"Doe\",\n\t\"code\":\"0001\"\n}", decodedJsonContent);
	}

}