package com.example.androidnavigationdrawer;




import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidnavigationdrawer.DatabaseHelper;
import com.example.androidnavigationdrawer.Client;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ClientListFragment extends Fragment {

    private ListView listViewClients;
    private FloatingActionButton fabAddClient;
    private DatabaseHelper databaseHelper;
    private List<Client> clientList;
    private ArrayAdapter<Client> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(getContext());

        // Initialize views
        listViewClients = view.findViewById(R.id.listViewClients);
        fabAddClient = view.findViewById(R.id.fabAddClient);

        // Load clients
        loadClients();

        // Set up click listeners
        fabAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddClientDialog();
            }
        });

        listViewClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Client selectedClient = clientList.get(position);
                showClientOptionsDialog(selectedClient);
            }
        });

        return view;
    }

    private void loadClients() {
        clientList = databaseHelper.getAllClients();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, clientList);
        listViewClients.setAdapter(adapter);
    }

    private void showAddClientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Client");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_client_form, null);
        final EditText editTextName = viewInflated.findViewById(R.id.editTextClientName);
        final EditText editTextEmail = viewInflated.findViewById(R.id.editTextClientEmail);
        final EditText editTextPhone = viewInflated.findViewById(R.id.editTextClientPhone);

        builder.setView(viewInflated);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Client newClient = new Client(name, email, phone);
                long id = databaseHelper.addClient(newClient);

                if (id != -1) {
                    Toast.makeText(getContext(), "Client added successfully", Toast.LENGTH_SHORT).show();
                    loadClients(); // Refresh the list
                } else {
                    Toast.makeText(getContext(), "Failed to add client", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showClientOptionsDialog(final Client client) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(client.getName());
        builder.setItems(new String[]{"View Profile", "Edit", "Delete"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // View Profile
                        openClientProfile(client);
                        break;
                    case 1: // Edit
                        showEditClientDialog(client);
                        break;
                    case 2: // Delete
                        showDeleteConfirmationDialog(client);
                        break;
                }
            }
        });
        builder.show();
    }

    private void openClientProfile(Client client) {
        Intent intent = new Intent(getActivity(), CustomerProfileActivity.class);
        intent.putExtra("client_id", client.getId());
        startActivity(intent);
    }

    private void showEditClientDialog(final Client client) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Client");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_client_form, null);
        final EditText editTextName = viewInflated.findViewById(R.id.editTextClientName);
        final EditText editTextEmail = viewInflated.findViewById(R.id.editTextClientEmail);
        final EditText editTextPhone = viewInflated.findViewById(R.id.editTextClientPhone);

        // Pre-fill with client data
        editTextName.setText(client.getName());
        editTextEmail.setText(client.getEmail());
        editTextPhone.setText(client.getPhone());

        builder.setView(viewInflated);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                client.setName(name);
                client.setEmail(email);
                client.setPhone(phone);

                int result = databaseHelper.updateClient(client);
                if (result > 0) {
                    Toast.makeText(getContext(), "Client updated successfully", Toast.LENGTH_SHORT).show();
                    loadClients(); // Refresh the list
                } else {
                    Toast.makeText(getContext(), "Failed to update client", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showDeleteConfirmationDialog(final Client client) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Client");
        builder.setMessage("Are you sure you want to delete " + client.getName() + "?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteClient(client);
                Toast.makeText(getContext(), "Client deleted", Toast.LENGTH_SHORT).show();
                loadClients(); // Refresh the list
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}