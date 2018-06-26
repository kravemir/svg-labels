package org.kravemir.svg.labels.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XPathMatcher extends TypeSafeMatcher<Document> {
    private static final XPath XPATH = XPathFactory.newInstance().newXPath();

    public static XPathMatcher matchesXPath(int count, String xpath) {
        return new XPathMatcher(count, xpath);
    }

    private final int count;
    private final String rule;

    public XPathMatcher(int count, String rule) {
        this.count = count;
        this.rule = rule;
    }

    @Override
    protected boolean matchesSafely(Document document) {
        return count == getCount(document, rule);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matches " + count + " times `" + rule + "`");
    }

    @Override
    protected void describeMismatchSafely(Document document, Description mismatchDescription) {
        mismatchDescription.appendText("rule matched document " + getCount(document, rule) + " time(s)");
    }

    private int getCount(Document doc, String expression) {
        try {
            return ((NodeList) XPATH.evaluate(expression, doc, XPathConstants.NODESET)).getLength();
        } catch (XPathExpressionException e) {
            throw new RuntimeException("This shouldn't happen", e);
        }
    }
}