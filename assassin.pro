-insert {
  **.all;
#  *.<init>;
#  *.onClick;
}

-replace {
#  *.show;
}

-receiver {
  com.meetyou.aop.assassin.TestDelegate;
}