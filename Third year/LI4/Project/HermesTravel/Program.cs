using DataAccessHermesTravel;
using HermesTravel.Data;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorPages();
builder.Services.AddServerSideBlazor();
builder.Services.AddSingleton<WeatherForecastService>();
builder.Services.AddSingleton<ISqlDataAccess, SqlDataAccess>();
builder.Services.AddScoped<User>();
builder.Services.AddScoped<Admin>();
builder.Services.AddCors();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error");
}

app.UseStaticFiles();

app.UseRouting();

app.UseCors(options =>
     options.WithOrigins("http://localhost:5283")
            .AllowAnyHeader()
            .AllowAnyMethod());

app.MapBlazorHub();
app.MapFallbackToPage("/_Host");

app.Run();
