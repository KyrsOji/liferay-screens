﻿using System;
using UIKit;
using LiferayScreens;
using Foundation;

namespace ShowcaseiOS.ViewController
{
        public partial class RatingViewController : UIViewController, IRatingScreenletDelegate
        {
        public RatingViewController(IntPtr handle) : base(handle) { }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();

            this.ratingScreenletThumbs.ClassName = "com.liferay.document.library.kernel.model.DLFileEntry";
            this.ratingScreenletThumbs.ClassPK = 57177;
            this.ratingScreenletThumbs.Editable = true;
            this.ratingScreenletThumbs.ThemeName = "default-thumbs";

            this.ratingScreenletStars.ClassName = "com.liferay.document.library.kernel.model.DLFileEntry";
            this.ratingScreenletStars.ClassPK = 57177;
            this.ratingScreenletStars.Editable = true;
            this.ratingScreenletStars.ThemeName = "default-stars";

            //FIXME: RatingScreenlet doesn't work with emojis view
            //this.ratingScreenletStars.ClassName = "com.liferay.document.library.kernel.model.DLFileEntry";
            //this.ratingScreenletStars.ClassPK = 57177;
            //this.ratingScreenletStars.Editable = true;
            //this.ratingScreenletStars.ThemeName = "default-emojis";

            this.ratingScreenletThumbs.Delegate = this;
            this.ratingScreenletStars.Delegate = this;
        }

        /* IRatingScreenletDelegate */

        [Export("screenlet:onRatingDeleted:")]
        public virtual void OnRatingDeleted(RatingScreenlet screenlet, RatingEntry rating)
        {
            Console.WriteLine("Rating delete");
        }

        [Export("screenlet:onRatingError:")]
        public virtual void OnRatingError(RatingScreenlet screenlet, NSError error)
        {
            Console.WriteLine($"Rating error: {error.Description}");
        }

        [Export("screenlet:onRatingRetrieve:")]
        public virtual void OnRatingRetrieve(RatingScreenlet screenlet, RatingEntry rating)
        {
            Console.WriteLine($"Rating retrive: {rating.Attributes}");
        }

        [Export("screenlet:onRatingUpdated:")]
        public virtual void OnRatingUpdated(RatingScreenlet screenlet, RatingEntry rating)
        {
            Console.WriteLine($"Rating updated: {rating.Average}");
        }
    }
}
