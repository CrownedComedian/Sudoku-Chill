package cs.crownedcomedian.sudokuchill.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cs.crownedcomedian.sudokuchill.R;
import cs.crownedcomedian.sudokuchill.model.SudokuAchievement;

public class AchievementsSearchActivity extends AppCompatActivity {

    private class AchievementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SudokuAchievement achievement;

        private final TextView title;
        private final TextView description;
        private ImageView icon = null;

        AchievementViewHolder(View view) {
            super(view);

            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.achievement_title);
            description = itemView.findViewById(R.id.achievement_description);
            icon = itemView.findViewById(R.id.achievement_icon);
        }

        private void bind(SudokuAchievement a) {
            this.achievement = a;
            title.setText(a.getTitle());
            description.setText(a.getDescription());
            icon.setImageDrawable(a.getIcon(getApplicationContext()));
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class AchievementAdapter extends RecyclerView.Adapter<AchievementViewHolder> implements Filterable {
        private final List<SudokuAchievement> visibleAchievements = new ArrayList<>();

        @NotNull
        @Override
        public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_achievement, parent, false);

            return new AchievementViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
            holder.bind(visibleAchievements.get(position));
        }

        @Override
        public int getItemCount() {
            return visibleAchievements.size();
        }

        Filter searchFilter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<SudokuAchievement> filterResults = new ArrayList<>();

                if(constraint != null || constraint.length() != 0) {
                    String filter = constraint.toString().toLowerCase().trim();
                    //TODO - filter
//                    for(Person p : DataCache.getInstance().people) {
//                        if(p.firstName.toLowerCase().contains(filter) || p.lastName.toLowerCase().contains(filter)) {
//                            filterResults.add(p);
//                        }
//                    }
//
//                    for(Event e : DataCache.getInstance().filteredEvents) {
//                        if(e.country.toLowerCase().contains(filter)   || e.city.toLowerCase().contains(filter) ||
//                                e.eventType.toLowerCase().contains(filter) || String.valueOf(e.year).contains(filter)) {
//                            filterResults.add(e);
//                        }
//                    }
                }

                FilterResults results = new FilterResults();
                results.values = filterResults;

                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                visibleAchievements.clear();

                if(results.values != null) {
                    for(SudokuAchievement a : (List<SudokuAchievement>)results.values) {
                        visibleAchievements.add(a);
                    }
                }

                notifyDataSetChanged();
            }
        };

        @Override
        public Filter getFilter() {
            return searchFilter;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.searchDataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(AchievementsSearchActivity.this));

        AchievementAdapter adapter = new AchievementAdapter();
        recyclerView.setAdapter(adapter);

        SearchView searchBar = (SearchView)findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
