package orbital.raspberry.neighber;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class HistoryRequestList extends ArrayAdapter<OfferToLendPost> {

    private Activity context;
    List<OfferToLendPost> offers;

    public HistoryRequestList(Activity context, List<OfferToLendPost> offers) {
        super(context, R.layout.layout_records_list, offers);
        this.context = context;
        this.offers = offers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_records_list, null, true);

        TextView itemname = (TextView) listViewItem.findViewById(R.id.itemnameTxt);
        TextView offernum = (TextView) listViewItem.findViewById(R.id.offernumTxt);
        TextView status = (TextView) listViewItem.findViewById(R.id.statusTxt);

        OfferToLendPost offer = offers.get(position);
        itemname.setText(offer.getItemname());

        status.setText("");
        offernum.setText("Borrowed From: " + offer.getTargetname());

        return listViewItem;
    }

}
