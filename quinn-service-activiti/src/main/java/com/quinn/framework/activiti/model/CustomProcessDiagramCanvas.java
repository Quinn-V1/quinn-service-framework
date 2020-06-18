package com.quinn.framework.activiti.model;

import org.activiti.bpmn.model.AssociationDirection;
import org.activiti.image.impl.DefaultProcessDiagramCanvas;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

/**
 * BPM实例流程图自定义花瓣
 *
 * @author Simon.z
 * @since 2020/6/10
 */
public class CustomProcessDiagramCanvas extends DefaultProcessDiagramCanvas {

    private static final String CONNECTION_TYPE_ASSOCIATION = "association";

    /**
     * 活动节点颜色
     */
    private Color activeColor;

    /**
     * 历史节点颜色
     */
    private Color historyColor;

    public CustomProcessDiagramCanvas(
            int width, int height, int minX, int minY,
            String activityFontName, String labelFontName, String annotationFontName,
            int activeColor, int historyColor
    ) {
        super(width, height, minX, minY, activityFontName, labelFontName, annotationFontName);
        this.activeColor = new Color(activeColor);
        this.historyColor = new Color(historyColor);
    }

    @Override
    public void drawConnection(
            int[] xPoints, int[] yPoints,
            boolean conditional, boolean isDefault,
            String connectionType, AssociationDirection associationDirection, boolean highLighted
    ) {

        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();

        g.setPaint(CONNECTION_COLOR);

        if (CONNECTION_TYPE_ASSOCIATION.equals(connectionType)) {
            g.setStroke(ASSOCIATION_STROKE);
        } else if (highLighted) {
            // 连线高亮
            g.setPaint(historyColor);
            g.setStroke(HIGHLIGHT_FLOW_STROKE);
        }

        for (int i = 1; i < xPoints.length; i++) {
            Integer sourceX = xPoints[i - 1];
            Integer sourceY = yPoints[i - 1];
            Integer targetX = xPoints[i];
            Integer targetY = yPoints[i];
            Line2D.Double line = new Line2D.Double(sourceX, sourceY, targetX, targetY);
            g.draw(line);
        }

        if (isDefault) {
            Line2D.Double line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            drawDefaultSequenceFlowIndicator(line);
        }

        if (conditional) {
            Line2D.Double line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            drawConditionalSequenceFlowIndicator(line);
        }

        if (associationDirection.equals(AssociationDirection.ONE)
                || associationDirection.equals(AssociationDirection.BOTH)) {
            Line2D.Double line = new Line2D.Double(
                    xPoints[xPoints.length - 2], yPoints[xPoints.length - 2],
                    xPoints[xPoints.length - 1], yPoints[xPoints.length - 1]);
            drawArrowHead(line);
        }
        if (associationDirection.equals(AssociationDirection.BOTH)) {
            Line2D.Double line = new Line2D.Double(xPoints[1], yPoints[1], xPoints[0], yPoints[0]);
            drawArrowHead(line);
        }
        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }

    @Override
    public void drawHighLight(
            int x, int y, int width, int height
    ) {
        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();

        g.setPaint(activeColor);
        g.setStroke(THICK_TASK_BORDER_STROKE);

        RoundRectangle2D rect = new RoundRectangle2D.Double(
                x, y, width, height, 20, 20);
        g.draw(rect);

        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }

    /**
     * 历史节点节点高亮
     *
     * @param x      x轴
     * @param y      y轴
     * @param width  宽
     * @param height 高
     */
    public void drawGreenHighLight(
            int x, int y, int width, int height
    ) {
        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();

        g.setPaint(historyColor);
        g.setStroke(THICK_TASK_BORDER_STROKE);

        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
        g.draw(rect);

        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }

    public static void main(String[] args) {
        int r = 139;
        int g = 0;
        int b = 0;
        int value = ((255 & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                ((b & 0xFF) << 0);

        System.out.println(new Color(value));
        System.out.println(value);
    }
}
