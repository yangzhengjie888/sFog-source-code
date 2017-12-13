// Generated code from Butter Knife. Do not modify!
package com.example.androidtemplate;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SocketActivity$$ViewBinder<T extends com.example.androidtemplate.SocketActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361832, "field 'downloadBtn' and method 'onClick'");
    target.downloadBtn = finder.castView(view, 2131361832, "field 'downloadBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361838, "field 'resultTv'");
    target.resultTv = finder.castView(view, 2131361838, "field 'resultTv'");
    view = finder.findRequiredView(source, 2131361795, "field 'rightTv' and method 'onClick'");
    target.rightTv = finder.castView(view, 2131361795, "field 'rightTv'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361831, "field 'uploadBtn' and method 'onClick'");
    target.uploadBtn = finder.castView(view, 2131361831, "field 'uploadBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361796, "field 'contentLl'");
    target.contentLl = finder.castView(view, 2131361796, "field 'contentLl'");
    view = finder.findRequiredView(source, 2131361837, "field 'msgTv'");
    target.msgTv = finder.castView(view, 2131361837, "field 'msgTv'");
    view = finder.findRequiredView(source, 2131361792, "field 'titleLl'");
    target.titleLl = finder.castView(view, 2131361792, "field 'titleLl'");
    view = finder.findRequiredView(source, 2131361793, "field 'leftTv' and method 'onClick'");
    target.leftTv = finder.castView(view, 2131361793, "field 'leftTv'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361794, "field 'titleTv'");
    target.titleTv = finder.castView(view, 2131361794, "field 'titleTv'");
    view = finder.findRequiredView(source, 2131361834, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361835, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361833, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361836, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.downloadBtn = null;
    target.resultTv = null;
    target.rightTv = null;
    target.uploadBtn = null;
    target.contentLl = null;
    target.msgTv = null;
    target.titleLl = null;
    target.leftTv = null;
    target.titleTv = null;
  }
}
