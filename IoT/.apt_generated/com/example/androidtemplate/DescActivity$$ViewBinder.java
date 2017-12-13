// Generated code from Butter Knife. Do not modify!
package com.example.androidtemplate;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DescActivity$$ViewBinder<T extends com.example.androidtemplate.DescActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
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
    view = finder.findRequiredView(source, 2131361841, "field 'frequencyTt'");
    target.frequencyTt = finder.castView(view, 2131361841, "field 'frequencyTt'");
    view = finder.findRequiredView(source, 2131361840, "field 'bssidEt'");
    target.bssidEt = finder.castView(view, 2131361840, "field 'bssidEt'");
    view = finder.findRequiredView(source, 2131361839, "field 'ssidTv'");
    target.ssidTv = finder.castView(view, 2131361839, "field 'ssidTv'");
    view = finder.findRequiredView(source, 2131361842, "field 'leaveTv'");
    target.leaveTv = finder.castView(view, 2131361842, "field 'leaveTv'");
    view = finder.findRequiredView(source, 2131361796, "field 'contentLl'");
    target.contentLl = finder.castView(view, 2131361796, "field 'contentLl'");
    view = finder.findRequiredView(source, 2131361792, "field 'titleLl'");
    target.titleLl = finder.castView(view, 2131361792, "field 'titleLl'");
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
  }

  @Override public void unbind(T target) {
    target.leftTv = null;
    target.titleTv = null;
    target.frequencyTt = null;
    target.bssidEt = null;
    target.ssidTv = null;
    target.leaveTv = null;
    target.contentLl = null;
    target.titleLl = null;
    target.rightTv = null;
  }
}
