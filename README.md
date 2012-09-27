SlidingMenuLayout
=================

A custom layout for Android that reveals a menu lying underneath another view.


Description
-----------
This project is aimed to provide a very basic implementation of a layout to
implement sliding menu's like those found in the Facebook, Spotify or Firefox
apps, for example.


Objectives
----------

 * **Simplicity** - This solution only requires adding a single class to your
   own project, and can be used with a minimal amount of glue code.
 * **Flexibility** - This solution only provides the bare minimum to implement a
   sliding menu. You will have maximum flexibility to go from there (in fact,
   the bottom view that is revealed doesn't even really have to be a menu).
 * **Compatibility** - The solution works on old Android versions. Personally I
   make sure it works at least on Android 2.2. Also, I make sure this solution
   can be used as a drop-in solution with PhoneGap projects.


Usage
-----

First, copy the file SlidingMenuLayout.java into your project.

Second, use the SlidingMenuLayout as root view for your Activity. To accomplish
this with the least amount of modification to existing code, you can override
your Activity's setContentView() as follows:

```java
    @Override
    public void setContentView(View view) {

        SlidingMenuLayout layout = new SlidingMenuLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT, 0.0F));

        layout.addView(new MenuView(this));

        layout.addView(view);

        super.setContentView(layout);
    }
```

In this example, MenuView is the view that will actually show the menu. It is up
to you to implement this view.

Finally, you can add a button (typically in the top left corner of your main
view), that calls openMenu() or closeMenu() on the layout as appropriate. This
also is left as an exercise to the reader.
